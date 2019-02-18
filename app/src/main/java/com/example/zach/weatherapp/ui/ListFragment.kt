package com.example.zach.weatherapp.ui



import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zach.weatherapp.Adapter.CityAdapter
import com.example.zach.weatherapp.R
import com.example.zach.weatherapp.utils.Injectable
import com.example.zach.weatherapp.viewModel.CityListViewModel
import timber.log.Timber
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 *
 */
class ListFragment : Fragment(), Injectable {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    private lateinit var progressView: ProgressBar

    lateinit var viewModel: CityListViewModel

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    @SuppressLint("RestrictedApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<com.example.zach.weatherapp.databinding.FragmentListBinding>(inflater,R.layout.fragment_list, container, false)
        viewManager = LinearLayoutManager(activity)
        recyclerView = binding.list
        progressView = binding.progressBar


        viewModel = ViewModelProviders.of(this,factory).get(CityListViewModel::class.java)

        val coordinates = getLocation()
        val latitude = coordinates[0]
        val longitude = coordinates[1]


        if(isOnline()) {
            binding.emptyStateTextView.visibility = View.GONE
            showProgress(true)

            viewModel.getCities(latitude, longitude).observe(this, Observer { cities ->
                Timber.d("Observing cities : %s", cities.toString())
                viewAdapter = CityAdapter(cities)
                recyclerView.apply {
                    // use a linear layout manager
                    layoutManager = viewManager
                    // specify a viewAdapter
                    adapter = viewAdapter
                }
                showProgress(false)
            })
        }else{
            binding.emptyStateTextView.visibility = View.VISIBLE
            binding.listFragment.visibility = View.GONE
        }

        setHasOptionsMenu(true)
        return binding.root
    }

    fun getLocation(): List<Double>{
        val sharedPreferences = activity?.getSharedPreferences("My prefs" ,0)
        return listOf(sharedPreferences?.getString("lat","48.85341")!!.toDouble(),sharedPreferences.getString("lng","2.3488")!!.toDouble())
    }

    fun isOnline(): Boolean {
        val connMgr = activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo? = connMgr.activeNetworkInfo
        return networkInfo?.isConnected == true
    }

    private fun showProgress(show: Boolean) {
        val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime)

        recyclerView.visibility = if (show) View.GONE else View.VISIBLE
        recyclerView.animate().setDuration(shortAnimTime.toLong()).alpha(
            (if (show) 0 else 1).toFloat()).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                recyclerView.visibility = if (show) View.GONE else View.VISIBLE
            }
        })

        progressView.visibility = if (show) View.VISIBLE else View.GONE
        progressView.animate().setDuration(shortAnimTime.toLong()).alpha(
            (if (show) 1 else 0).toFloat()).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                progressView.visibility = if (show) View.VISIBLE else View.GONE
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu,menu)

        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.search).actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
            setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextChange(newText: String?): Boolean {
                    return true
                }

                override fun onQueryTextSubmit(query: String?): Boolean {
                    viewModel.getCityByName(query!!).observe(this@ListFragment, Observer { city ->
                        viewAdapter = CityAdapter(listOf(city))
                        recyclerView.apply {
                            // use a linear layout manager
                            layoutManager = viewManager
                            // specify a viewAdapter
                            adapter = viewAdapter
                        }
                    })

                    return false
                }

            })
        }
        super.onCreateOptionsMenu(menu, inflater)
    }
}
