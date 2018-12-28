package com.example.zach.weatherapp.ui


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.zach.weatherapp.R
import com.example.zach.weatherapp.data.Forecast
import com.example.zach.weatherapp.viewModel.ForecastViewModel
import kotlinx.android.synthetic.main.fragment_forecast_details.*


/**
 * A simple [Fragment] subclass.
 *
 */
class ForecastDetailsFragment : Fragment() {

    private val cityID =  2172797
    private lateinit var viewModel: ForecastViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ForecastViewModel::class.java!!)
        viewModel.init(cityID)

        viewModel.getForecast().observe(this, Observer {forecast: Forecast -> temperature_textview.text=forecast.temperature.toString()})
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forecast_details, container, false)
    }


}
