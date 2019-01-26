package com.example.zach.weatherapp.utils


import android.util.Log
import com.example.zach.weatherapp.data.OpenWeatherCycleDataResponse
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherCache @Inject constructor(){

    private var cache: Single<OpenWeatherCycleDataResponse>? = null

    companion object {
        private val TAG = "WeatherCache"
    }

    init {
        Log.d(TAG,"brand new cache")
    }

    fun getCachedCities():Single<OpenWeatherCycleDataResponse>?{
        Log.d(TAG, "returning data from cache..."+cache.toString())
        return cache
    }



    fun put(data: Single<OpenWeatherCycleDataResponse>?){
        cache = data
        Log.d(TAG, "putting data in cache..."+cache.toString())
    }
}