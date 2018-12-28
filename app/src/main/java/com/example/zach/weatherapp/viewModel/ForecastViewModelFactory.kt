package com.example.zach.weatherapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.zach.weatherapp.data.ForecastRepository

class ForecastViewModelFactory(private val forecastRepository: ForecastRepository) :
    ViewModelProvider.NewInstanceFactory(){

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ForecastViewModel(forecastRepository) as T
    }
}