package com.example.zach.weatherapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.zach.weatherapp.data.ForecastRepository

class CityListViewModelFactory(private val forecastRepository: ForecastRepository) :
    ViewModelProvider.NewInstanceFactory(){

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CityListViewModel(forecastRepository) as T
    }
}

