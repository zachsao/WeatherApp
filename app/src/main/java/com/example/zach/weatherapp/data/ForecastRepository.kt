package com.example.zach.weatherapp.data


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import com.example.zach.weatherapp.utils.OpenWeatherApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


class ForecastRepository {

    private var service: OpenWeatherApi
    private val API_KEY = "106f229f3c174ddd91281c21ff5783da"

    companion object {
        @Volatile private var instance: ForecastRepository? = null

        fun getInstance() =
            instance ?: synchronized(this){
                instance?: ForecastRepository().also {instance = it}
            }
    }

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://api.openweathermap.org")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        service = retrofit.create(OpenWeatherApi::class.java)
    }

    fun getForecast(cityId: Int): LiveData<Forecast> {
        // This isn't an optimal implementation. We'll fix it later.
        val data = MutableLiveData<Forecast>()
        //dummy forecast
        val forecast = Forecast(14.0,1010,94,9.0,18.0,"drizzle")
        data.value = forecast
        return data
    }

    fun getCities(): MutableLiveData<List<City>>{
        val data = MutableLiveData<List<City>>()
        service.getCities(API_KEY).enqueue(object : Callback<OpenWeatherCycleDataResponse> {
            override fun onResponse(call: Call<OpenWeatherCycleDataResponse>,
                                    response: Response<OpenWeatherCycleDataResponse>){
                val cities = response.body()?.list?.map {city ->
                    City(city.id,city.name,city.coord,city.weather,city.main)
                }
                data.value = cities
            }
            override fun onFailure(call: Call<OpenWeatherCycleDataResponse>, t: Throwable) {
                System.out.println(t.message)
                //To change body of created functions use File | Settings | File Templates.
            }
        })

        return data
    }
}