package com.example.zach.weatherapp.ui


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zach.weatherapp.Adapter.ForecastAdapter
import com.example.zach.weatherapp.R
import com.example.zach.weatherapp.utils.injectorUtils
import com.example.zach.weatherapp.viewModel.CityListViewModel


/**
 * A simple [Fragment] subclass.
 *
 */
class ListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager


    private val TAG = "ListFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_list, container, false)
        viewManager = LinearLayoutManager(activity)
        recyclerView = rootView.findViewById(R.id.list)


        val factory = injectorUtils.provideCityListViewModelFactory()

        val viewModel = ViewModelProviders.of(this,factory).get(CityListViewModel::class.java)

        viewModel.getCities().observe(this, Observer { cities ->
            //cities.forEach { city -> myDataset.add(city) }
            var myDataset = cities
            Log.d(TAG,cities.toString())
            viewAdapter = ForecastAdapter(myDataset)
            recyclerView.apply {
                // use a linear layout manager
                layoutManager = viewManager
                // specify a viewAdapter
                adapter = viewAdapter
            }
        })


        return rootView
    }
}
