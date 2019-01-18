package com.example.zach.weatherapp.utils


import com.example.zach.weatherapp.data.OpenWeatherCycleDataResponse
import io.reactivex.Single

class WeatherCache{

    private var cache: Single<OpenWeatherCycleDataResponse>? = null

    fun getCachedCities():Single<OpenWeatherCycleDataResponse>?{
        return cache
    }



    fun put(data: Single<OpenWeatherCycleDataResponse>?){
        cache = data
    }
}