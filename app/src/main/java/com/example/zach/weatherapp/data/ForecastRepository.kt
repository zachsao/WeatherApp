package com.example.zach.weatherapp.data


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData


class ForecastRepository {

    companion object {
        @Volatile private var instance: ForecastRepository? = null

        fun getInstance() =
            instance ?: synchronized(this){
                instance?: ForecastRepository().also {instance = it}
            }
    }

    fun getForecast(cityId: Int): LiveData<Forecast> {
        // This isn't an optimal implementation. We'll fix it later.
        val data = MutableLiveData<Forecast>()
        //dummy forecast
        val forecast = Forecast("cloudy",13)
        data.value = forecast
        return data
    }
}