package com.example.zach.weatherapp.ui


import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zach.weatherapp.Adapter.CityAdapter
import com.example.zach.weatherapp.Adapter.ForecastAdapter
import com.example.zach.weatherapp.R
import com.example.zach.weatherapp.data.City
import com.example.zach.weatherapp.data.Forecast
import com.example.zach.weatherapp.data.Weather
import com.example.zach.weatherapp.data.WeeklyForecast
import com.example.zach.weatherapp.databinding.FragmentForecastDetailsBinding
import com.example.zach.weatherapp.utils.GlideApp
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
    lateinit var binding: FragmentForecastDetailsBinding

    //weekly forecast
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_forecast_details, container, false)

        viewManager = LinearLayoutManager(activity)
        recyclerView = binding.forecastRecyclerView

        val cityID = ForecastDetailsFragmentArgs.fromBundle(arguments!!).cityId

        viewModel = ViewModelProviders.of(this,factory).get(ForecastViewModel::class.java)


        displayData(cityID)

        return binding.root
    }

    fun displayData(cityId: Int){

        viewModel.getDetailedWeatherInfo(cityId).observe(this, Observer {
                weatherInfo: City? ->
            Timber.d("Displaying city : %s",weatherInfo?.name)
            if (weatherInfo != null) {
                binding.city = weatherInfo
                binding.temperatureTextview.text = "${weatherInfo.main.temp.toInt()}°"
                binding.maxTemperatureTextview.text = "${weatherInfo.main.temp_max.toInt()}°"
                binding.minTemperatureTextview.text = "${weatherInfo.main.temp_min.toInt()}°"
                (activity as AppCompatActivity).supportActionBar?.title = weatherInfo.name

                if(isOnline()){
                    GlideApp.with(activity as AppCompatActivity)
                        .load("http://openweathermap.org/img/w/${weatherInfo.weather[0].icon}.png")
                        .error(R.drawable.ic_error)
                        .into(imageView)
                }

            }

        })

        viewModel.getWeekForecast(cityId).observe(this, Observer { list ->
            Timber.d("$list")
            viewAdapter = ForecastAdapter(list)
            recyclerView.apply {
                // use a linear layout manager
                layoutManager = viewManager
                // specify a viewAdapter
                adapter = viewAdapter

                addItemDecoration(DividerItemDecoration(activity,LinearLayoutManager.VERTICAL))
            }
        })

    }

    fun isOnline(): Boolean {
        val connMgr = activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo? = connMgr.activeNetworkInfo
        return networkInfo?.isConnected == true
    }

}
