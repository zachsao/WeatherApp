package com.example.zach.weatherapp.utils.Components

import android.app.Application
import com.example.zach.weatherapp.MainActivity
import com.example.zach.weatherapp.WeatherApp
import com.example.zach.weatherapp.ui.ForecastDetailsFragment
import com.example.zach.weatherapp.ui.ListFragment
import com.example.zach.weatherapp.utils.Modules.AppModule
import com.example.zach.weatherapp.utils.Modules.MainActivityModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component (modules = [AndroidInjectionModule::class,AppModule::class, MainActivityModule::class])
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(app: WeatherApp)
}