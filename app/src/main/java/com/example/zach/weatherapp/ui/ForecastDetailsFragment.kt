package com.example.zach.weatherapp.ui


import android.app.SearchManager
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.zach.weatherapp.R
import com.example.zach.weatherapp.data.City
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_forecast_details, container, false)

        val cityID = ForecastDetailsFragmentArgs.fromBundle(arguments!!).cityId

        viewModel = ViewModelProviders.of(this,factory).get(ForecastViewModel::class.java)


        displayData(cityID)

        setHasOptionsMenu(true)

        return binding.root
    }

    fun displayData(cityId: Int){

        val coordinates = getLocation()
        val latitude = coordinates[0]
        val longitude = coordinates[1]

        viewModel.getDetailedWeatherInfo(cityId,latitude,longitude).observe(this, Observer {
                weatherInfo: City? ->
            Timber.d("Displaying city : %s",weatherInfo?.name)
            if (weatherInfo != null) {
                binding.city = weatherInfo
                (activity as AppCompatActivity).supportActionBar?.title = weatherInfo.name

                if(isOnline()){
                    GlideApp.with(activity as AppCompatActivity)
                        .load("http://openweathermap.org/img/w/${weatherInfo.weather[0].icon}.png")
                        .error(R.drawable.sunny)
                        .into(imageView)
                }

            }

        })
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu,menu)

        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.search).actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))

        }
        super.onCreateOptionsMenu(menu, inflater)
    }

}
