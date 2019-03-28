package com.example.zach.weatherapp.ui



import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zach.weatherapp.Adapter.CityAdapter
import com.example.zach.weatherapp.R
import com.example.zach.weatherapp.databinding.FragmentListBinding
import com.example.zach.weatherapp.utils.Injectable
import com.example.zach.weatherapp.viewModel.CityListViewModel
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import timber.log.Timber
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 *
 */
class ListFragment : Fragment(), Injectable {

    companion object {
        const val MY_PERMISSIONS_REQUEST_ACCESS_LOCATION = 1
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    private lateinit var progressView: ProgressBar

    lateinit var viewModel: CityListViewModel

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var binding: FragmentListBinding

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if(!hasLocationPermission())
            requestLocationPermission()
    }

    @SuppressLint("RestrictedApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_list, container, false)
        viewManager = LinearLayoutManager(activity)
        recyclerView = binding.list
        progressView = binding.progressBar


        viewModel = ViewModelProviders.of(this,factory).get(CityListViewModel::class.java)



        if(isOnline()) {
            viewModel.listenToLocations()
            displayList()
        }else{
            binding.emptyStateTextView.visibility = View.VISIBLE
            binding.listFragment.visibility = View.GONE
        }

        setHasOptionsMenu(true)
        return binding.root
    }

    private fun displayList() {
        binding.emptyStateTextView.visibility = View.GONE
        showProgress(true)

        viewModel.getCities().observe(this, Observer { cities ->
            Timber.i("Observing cities : %s", cities.toString())
            viewAdapter = CityAdapter(cities)
            recyclerView.apply {
                // use a linear layout manager
                layoutManager = viewManager
                // specify a viewAdapter
                adapter = viewAdapter
            }
            showProgress(false)
        })
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.refresh -> {

                if(isOnline()) {
                    viewModel.listenToLocations()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.startLocation()
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopLocation()
    }




    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            activity!!, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            MY_PERMISSIONS_REQUEST_ACCESS_LOCATION)

    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_ACCESS_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay! update the location
                    Timber.i("permission granted !")
                    viewModel.startLocation()
                    viewModel.listenToLocations()
                } else {
                    // permission denied, boo!
                    Timber.i("permission denied !")
                    Toast.makeText(activity,"To continue, enable permission in settings", LENGTH_SHORT).show()

                }
                return
            }
            else -> {
                // Ignore all other requests.
            }
        }
    }

}
