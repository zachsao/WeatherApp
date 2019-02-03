package com.example.zach.weatherapp.utils



import com.example.zach.weatherapp.data.OpenWeatherCycleDataResponse
import io.reactivex.Single
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherCache @Inject constructor(){

    private var cache: Single<OpenWeatherCycleDataResponse>? = null


    init {
        Timber.d("brand new cache")
    }

    fun getCachedCities():Single<OpenWeatherCycleDataResponse>?{
        Timber.d("returning data from cache : %s",cache.toString())
        return cache
    }



    fun put(data: Single<OpenWeatherCycleDataResponse>?){
        cache = data
        Timber.d("putting data in cache : %s",cache.toString())
    }
}