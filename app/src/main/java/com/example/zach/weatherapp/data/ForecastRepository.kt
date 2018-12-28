package com.example.zach.weatherapp.data


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData


class ForecastRepository {


    fun getForecast(cityId: Int): LiveData<Forecast> {
        // This isn't an optimal implementation. We'll fix it later.
        val data = MutableLiveData<Forecast>()

        return data
    }
}