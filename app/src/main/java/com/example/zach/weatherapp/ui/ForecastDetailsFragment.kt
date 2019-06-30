package com.example.zach.weatherapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zach.weatherapp.R
import com.example.zach.weatherapp.adapter.ForecastAdapter
import com.example.zach.weatherapp.data.ForecastViewItem
import com.example.zach.weatherapp.databinding.FragmentForecastDetailsBinding
import com.example.zach.weatherapp.utils.GlideApp
import com.example.zach.weatherapp.utils.Injectable
import com.example.zach.weatherapp.viewModel.ForecastViewModel
import kotlinx.android.synthetic.main.fragment_forecast_details.*
import timber.log.Timber
import javax.inject.Inject

class ForecastDetailsFragment : Fragment(), Injectable {

    lateinit var viewModel: ForecastViewModel
    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var binding: FragmentForecastDetailsBinding

    //weekly forecast
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_forecast_details, container, false)

        viewManager = LinearLayoutManager(activity)
        recyclerView = binding.forecastRecyclerView

        val cityID = ForecastDetailsFragmentArgs.fromBundle(arguments!!).cityId

        viewModel = ViewModelProviders.of(this, factory).get(ForecastViewModel::class.java)

        displayData(cityID)

        return binding.root
    }

    private fun displayData(cityId: Int) {

        viewModel.getDetailedWeatherInfo(cityId).observe(this, Observer { weatherInfo: ForecastViewItem ->
            Timber.d("Displaying city : %s", weatherInfo.name)
            binding.city = weatherInfo
            (activity as AppCompatActivity).supportActionBar?.title = weatherInfo.name

            GlideApp.with(activity as AppCompatActivity)
                .load(weatherInfo.icon)
                .error(R.drawable.ic_error)
                .into(imageView)
        })

        viewModel.getWeekForecast(cityId).observe(this, Observer { list ->
            Timber.d("$list")
            viewAdapter = ForecastAdapter(list)
            recyclerView.apply {
                // use a linear layout manager
                layoutManager = viewManager
                // specify a viewAdapter
                adapter = viewAdapter

                addItemDecoration(DividerItemDecoration(activity, LinearLayoutManager.VERTICAL))
            }
        })
    }
}
