package com.example.zach.weatherapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.zach.weatherapp.data.City
import androidx.lifecycle.MutableLiveData
import com.example.zach.weatherapp.data.ForecastRepository


class CityListViewModel(private var forecastRepo: ForecastRepository):ViewModel() {
    private lateinit var cities : MutableLiveData<List<City>>

    fun init() {
        cities = forecastRepo.getCities()
    }
    fun getCities(): LiveData<List<City>> {
        return cities
    }

}