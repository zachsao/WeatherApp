package com.example.zach.weatherapp.utils

import androidx.lifecycle.MutableLiveData
import com.example.zach.weatherapp.data.City

class WeatherCache{

    private var cache: MutableLiveData<List<City>>? = null

    fun getCachedCities():MutableLiveData<List<City>>?{
        return cache
    }

    fun put(data: MutableLiveData<List<City>>?){
        cache = data
    }
}