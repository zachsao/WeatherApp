package com.example.zach.weatherapp.data


import com.example.zach.weatherapp.BuildConfig
import com.example.zach.weatherapp.utils.OpenWeatherApi
import com.example.zach.weatherapp.utils.WeatherCache
import io.reactivex.Single
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ForecastRepository @Inject constructor(private var service :OpenWeatherApi, private var weatherCache: WeatherCache){

    private val API_KEY = BuildConfig.OPEN_WEATHER_MAP_API_KEY


    init {
        Timber.d("Repository initialized")
    }


    fun getCities(lat: Double, lon: Double): Single<OpenWeatherCycleDataResponse> {
        val cache = weatherCache.getCachedCities()
        if(cache!=null){
            Timber.d("getting cities from cache")
            return cache
        }
        Timber.d("getting cities from API")
        val response = service.getCities(lat,lon,API_KEY)
        weatherCache.put(response)
        return response
    }


}