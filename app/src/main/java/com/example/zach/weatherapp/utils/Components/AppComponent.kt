package com.example.zach.weatherapp.utils.Components

import com.example.zach.weatherapp.data.ForecastRepository
import com.example.zach.weatherapp.utils.Modules.AppModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component (modules = [AppModule::class])
interface AppComponent {

    fun getRepository(): ForecastRepository
}