package com.example.zach.weatherapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.zach.weatherapp.data.Forecast
import com.example.zach.weatherapp.data.ForecastRepository

class ForecastViewModel(private var forecastRepo: ForecastRepository ):ViewModel() {
    private lateinit var forecast:LiveData<Forecast>

    fun init(cityId: Int) {
        forecast = forecastRepo.getForecast(cityId)
    }

    fun getForecast(): LiveData<Forecast> {
        return forecast
    }
}