package com.example.zach.weatherapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.zach.weatherapp.data.City
import com.example.zach.weatherapp.data.Forecast
import com.example.zach.weatherapp.data.ForecastRepository

class ForecastViewModel(private var forecastRepo: ForecastRepository ):ViewModel() {
    private lateinit var forecast:LiveData<City>

    fun init(cityId: Int) {
        forecast = forecastRepo.getDetailedWeatherInfo(cityId)
    }

    fun getDetailedWeatherInfo(): LiveData<City> {
        return forecast
    }
}