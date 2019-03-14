package com.example.zach.weatherapp

import android.app.Activity
import android.app.Application
import androidx.multidex.MultiDexApplication
import com.example.zach.weatherapp.utils.AppInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import timber.log.Timber
import javax.inject.Inject

class WeatherApp: MultiDexApplication(), HasActivityInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()

        if(BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
            Timber.d("Timber tree planted !")
        }
        AppInjector.init(this)

    }

    override fun activityInjector() = dispatchingAndroidInjector

}