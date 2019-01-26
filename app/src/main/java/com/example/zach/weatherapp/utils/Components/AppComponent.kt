package com.example.zach.weatherapp.utils.Components

import com.example.zach.weatherapp.ui.ForecastDetailsFragment
import com.example.zach.weatherapp.ui.ListFragment
import com.example.zach.weatherapp.utils.Modules.AppModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component (modules = [AppModule::class])
interface AppComponent {

    fun inject(fragment: ListFragment)

    fun inject(fragment: ForecastDetailsFragment)
}