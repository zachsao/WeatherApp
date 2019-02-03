package com.example.zach.weatherapp

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.example.zach.weatherapp.utils.AppInjector
import com.example.zach.weatherapp.utils.Components.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class WeatherApp: Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        AppInjector.init(this)
    }

    override fun activityInjector() = dispatchingAndroidInjector

}