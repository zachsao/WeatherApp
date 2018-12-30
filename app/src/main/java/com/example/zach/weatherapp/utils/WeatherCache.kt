package com.example.zach.weatherapp.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.zach.weatherapp.data.City

class WeatherCache{

    private var cache: MutableLiveData<List<City>>? = null

    fun getCachedCities():MutableLiveData<List<City>>?{
        return cache
    }

    fun getSelectedCity(cityId: Int):City?{
        var selectedCity:City? = null
        getCachedCities()?.value?.forEach { city ->
            when(city.id){
                cityId -> selectedCity = city
            }
        }
        return selectedCity
    }

    fun put(data: MutableLiveData<List<City>>?){
        cache = data
    }
}