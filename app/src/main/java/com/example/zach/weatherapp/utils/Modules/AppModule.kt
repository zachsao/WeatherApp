package com.example.zach.weatherapp.utils.Modules

import com.example.zach.weatherapp.utils.OpenWeatherApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton


@Module
class AppModule {

    @Provides
    @Singleton
    fun provideApiService(): OpenWeatherApi{
        return Retrofit.Builder()
            .baseUrl("http://api.openweathermap.org")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create())
            .build().create(OpenWeatherApi::class.java)
    }
}