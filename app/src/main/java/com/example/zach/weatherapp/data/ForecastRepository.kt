package com.example.zach.weatherapp.data

import android.util.Log
import com.example.zach.weatherapp.BuildConfig
import com.example.zach.weatherapp.utils.OpenWeatherApi
import com.example.zach.weatherapp.utils.WeatherCache
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ForecastRepository @Inject constructor(private var service :OpenWeatherApi){


    private val API_KEY = BuildConfig.OPEN_WEATHER_MAP_API_KEY
    private var weatherCache = WeatherCache()

    companion object {
        private val TAG = "ForecastRepository"
    }



    fun getDetailedWeatherInfo(): Single<OpenWeatherCycleDataResponse>? = weatherCache.getCachedCities()

    fun getCities(): Single<OpenWeatherCycleDataResponse> {
        Log.d(TAG,"getting cities...")
        val response = service.getCities(API_KEY)
        weatherCache.put(response)
        return response
    }


}