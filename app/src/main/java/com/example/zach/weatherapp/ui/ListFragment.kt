package com.example.zach.weatherapp.ui



import android.app.SearchManager
import android.content.Context
import android.content.res.Resources
import android.location.LocationManager
import android.location.LocationProvider
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zach.weatherapp.Adapter.ForecastAdapter
import com.example.zach.weatherapp.MainActivity
import com.example.zach.weatherapp.R
import com.example.zach.weatherapp.utils.Injectable
import com.example.zach.weatherapp.viewModel.CityListViewModel

import kotlinx.android.synthetic.main.fragment_list.view.*
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

    //construct the ViewModelFactory and ViewModel with Dagger

    lateinit var viewModel: CityListViewModel

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<com.example.zach.weatherapp.databinding.FragmentListBinding>(inflater,R.layout.fragment_list, container, false)
        viewManager = LinearLayoutManager(activity)
        recyclerView = binding.list

        viewModel = ViewModelProviders.of(this,factory).get(CityListViewModel::class.java)

        val coordinates = getLocation()
        val latitude = coordinates[0]
        val longitude = coordinates[1]

        if(isOnline()) {
            binding.emptyStateTextView.visibility = View.GONE
            binding.listFragment.visibility = View.VISIBLE

            viewModel.getCities(latitude, longitude).observe(this, Observer { cities ->
                Timber.d("Observing cities : %s", cities.toString())
                viewAdapter = ForecastAdapter(cities)
                recyclerView.apply {
                    // use a linear layout manager
                    layoutManager = viewManager
                    // specify a viewAdapter
                    adapter = viewAdapter
                }
            })
        }else{
            binding.emptyStateTextView.visibility = View.VISIBLE
            binding.listFragment.visibility = View.GONE
        }
        return binding.root
    }

    fun getLocation(): List<Double>{
        val sharedPreferences = activity?.getSharedPreferences("My prefs" ,0)
        return listOf(sharedPreferences?.getString("lat","48.85341")!!.toDouble(),sharedPreferences.getString("lon","2.3488")!!.toDouble())
    }

    fun isOnline(): Boolean {
        val connMgr = activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo? = connMgr.activeNetworkInfo
        return networkInfo?.isConnected == true
    }


}
