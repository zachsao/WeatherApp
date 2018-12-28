package com.example.zach.weatherapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.zach.weatherapp.data.Forecast

class ForecastViewModel(private var forecast: LiveData<Forecast>, private var cityId: Int ):ViewModel() {

    fun init(cityId: Int) {
        this.cityId = cityId
    }

    fun getForecast(): LiveData<Forecast> {
        val data = MutableLiveData<Forecast>()
        val forecast = Forecast("sunny",13)
        data.value = forecast
        return data
    }
}