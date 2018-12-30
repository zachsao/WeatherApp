package com.example.zach.weatherapp.ui


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.zach.weatherapp.R
import com.example.zach.weatherapp.data.City
import com.example.zach.weatherapp.utils.injectorUtils
import com.example.zach.weatherapp.viewModel.ForecastViewModel
import kotlinx.android.synthetic.main.fragment_forecast_details.*


/**
 * A simple [Fragment] subclass.
 *
 */
class ForecastDetailsFragment : Fragment() {

    private lateinit var viewModel: ForecastViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val cityID = ForecastDetailsFragmentArgs.fromBundle(arguments!!).cityId
        val factory = injectorUtils.provideForecastViewModelFactory()
        viewModel = ViewModelProviders.of(this,factory).get(ForecastViewModel::class.java)
        viewModel.init(cityID)

        displayData()

        return inflater.inflate(R.layout.fragment_forecast_details, container, false)
    }

    fun displayData(){
        viewModel.getDetailedWeatherInfo().observe(this, Observer {weatherInfo: City ->
            temperature_textview.text= "${weatherInfo.main.temp}°C"
            weather_description_textview.text = weatherInfo.weather[0].description
            max_temperature_textview.text = "${weatherInfo.main.temp_max}°C"
            min_temperature_textview.text = "${weatherInfo.main.temp_min}°C"
            pressure_value_textview.text = "${weatherInfo.main.pressure} hPa"
            humidity_value_textview.text = "${weatherInfo.main.humidity}%"
            wind_value_textview.text = "${weatherInfo.wind.speed} m/s"
        })
    }


}
