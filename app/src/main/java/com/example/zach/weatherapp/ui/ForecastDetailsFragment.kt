package com.example.zach.weatherapp.ui


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.zach.weatherapp.R
import com.example.zach.weatherapp.data.City
import com.example.zach.weatherapp.utils.Injectable
import com.example.zach.weatherapp.viewModel.ForecastViewModel
import kotlinx.android.synthetic.main.fragment_forecast_details.*
import timber.log.Timber
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 *
 */
class ForecastDetailsFragment : Fragment(), Injectable {

    lateinit var viewModel: ForecastViewModel
    @Inject
    lateinit var factory: ViewModelProvider.Factory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val cityID = ForecastDetailsFragmentArgs.fromBundle(arguments!!).cityId

        viewModel = ViewModelProviders.of(this,factory).get(ForecastViewModel::class.java)


        displayData(cityID)

        return inflater.inflate(R.layout.fragment_forecast_details, container, false)
    }

    fun displayData(cityId: Int){
        viewModel.getDetailedWeatherInfo(cityId).observe(this, Observer {weatherInfo: City? ->
            Timber.d("Displaying city : %s",weatherInfo?.name)
            if (weatherInfo != null) {
                (activity as AppCompatActivity).supportActionBar?.title = weatherInfo.name
                temperature_textview.text= "${weatherInfo.main.temp.toInt()}°C"
                weather_description_textview.text = weatherInfo.weather[0].description
                max_temperature_textview.text = "${weatherInfo.main.temp_max}°C"
                min_temperature_textview.text = "${weatherInfo.main.temp_min}°C"
                pressure_value_textview.text = "${weatherInfo.main.pressure} hPa"
                humidity_value_textview.text = "${weatherInfo.main.humidity}%"
                wind_value_textview.text = "${weatherInfo.wind.speed} m/s"
                Glide.with(activity)
                    .load("http://openweathermap.org/img/w/${weatherInfo.weather[0].icon}.png")
                    .into(imageView)
            }

        })
    }


}
