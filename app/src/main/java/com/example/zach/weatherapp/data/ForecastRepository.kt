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
        val forecast = Forecast("cloudy",13,17,9)
        data.value = forecast
        return data
    }

    fun getCities(): MutableLiveData<List<City>>{
        val data = MutableLiveData<List<City>>()
        val cities = ArrayList<City>()
        cities.add(City("Paris",0,12324,86698,getForecast(0).value!!))
        cities.add(City("Londres",0,12324,86698,getForecast(0).value!!))
        cities.add(City("Caen",0,12324,86698,getForecast(0).value!!))

        data.value = cities
        return data
    }
}