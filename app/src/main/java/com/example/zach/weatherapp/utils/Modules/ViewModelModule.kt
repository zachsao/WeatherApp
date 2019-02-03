package com.example.zach.weatherapp.utils.Modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.zach.weatherapp.viewModel.CityListViewModel
import com.example.zach.weatherapp.viewModel.CityListViewModelFactory
import com.example.zach.weatherapp.viewModel.ForecastViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(CityListViewModel::class)
    abstract fun bindRepoViewModel(repoViewModel: CityListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ForecastViewModel::class)
    abstract fun bindRepoViewModel(repoViewModel: ForecastViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: CityListViewModelFactory): ViewModelProvider.Factory
}