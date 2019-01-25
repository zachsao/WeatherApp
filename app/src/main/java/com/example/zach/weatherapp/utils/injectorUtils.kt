package com.example.zach.weatherapp.utils


import com.example.zach.weatherapp.utils.Components.DaggerAppComponent
import com.example.zach.weatherapp.viewModel.ForecastViewModelFactory

object injectorUtils {

    val component = DaggerAppComponent.create()

    fun provideForecastViewModelFactory(): ForecastViewModelFactory {
        val forecastRepository = component.getRepository()
        return ForecastViewModelFactory(forecastRepository)
   }
}