package com.example.zach.weatherapp.data

import android.util.Log
import com.example.zach.weatherapp.BuildConfig
import com.example.zach.weatherapp.utils.OpenWeatherApi
import com.example.zach.weatherapp.utils.WeatherCache
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ForecastRepository @Inject constructor(private var service :OpenWeatherApi, private var weatherCache: WeatherCache){


    private val API_KEY = BuildConfig.OPEN_WEATHER_MAP_API_KEY


    companion object {
        private val TAG = "ForecastRepository"
    }

    init {
        Log.d(TAG,"new repo")
    }


    fun getCities(): Single<OpenWeatherCycleDataResponse> {
        val cache = weatherCache.getCachedCities()
        if(cache!=null){
            Log.d(TAG, "getting cities from cache")
            return cache
        }
        Log.d(TAG,"getting cities from API")
        val response = service.getCities(API_KEY)
        weatherCache.put(response)
        return response
    }


}