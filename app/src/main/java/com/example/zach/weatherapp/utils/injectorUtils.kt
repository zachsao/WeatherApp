package com.example.zach.weatherapp.utils

import com.example.zach.weatherapp.data.ForecastRepository
import com.example.zach.weatherapp.viewModel.ForecastViewModelFactory

object injectorUtils {

    fun provideForecastViewModelFactory(): ForecastViewModelFactory {
        val forecastRepository = ForecastRepository.getInstance()
        return ForecastViewModelFactory(forecastRepository)
    }
}