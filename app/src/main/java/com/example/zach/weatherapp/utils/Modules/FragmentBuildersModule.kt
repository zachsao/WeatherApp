package com.example.zach.weatherapp.utils.Modules

import com.example.zach.weatherapp.ui.ForecastDetailsFragment
import com.example.zach.weatherapp.ui.ListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeListFragment(): ListFragment

    @ContributesAndroidInjector
    abstract fun contributeDetailsFragment(): ForecastDetailsFragment
}