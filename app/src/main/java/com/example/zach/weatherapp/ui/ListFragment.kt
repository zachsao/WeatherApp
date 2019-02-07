package com.example.zach.weatherapp.ui



import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zach.weatherapp.Adapter.ForecastAdapter
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

        viewModel.getCities().observe(this, Observer { cities ->
            Timber.d("Observing cities : %s",cities.toString())
            viewAdapter = ForecastAdapter(cities)
            recyclerView.apply {
                // use a linear layout manager
                layoutManager = viewManager
                // specify a viewAdapter
                adapter = viewAdapter
            }
        })
        return binding.root
    }

}
