package com.example.zach.weatherapp.data



import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData

import com.example.zach.weatherapp.BuildConfig
import com.example.zach.weatherapp.utils.OpenWeatherApi
import com.example.zach.weatherapp.utils.WeatherCache
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory


class ForecastRepository {



    private var service: OpenWeatherApi

    private val API_KEY = BuildConfig.OPEN_WEATHER_MAP_API_KEY
    private var weatherCache = WeatherCache()

    companion object {
        private val TAG = "ForecastRepository"
        @Volatile private var instance: ForecastRepository? = null

        fun getInstance() =
            instance ?: synchronized(this){
                instance?: ForecastRepository().also {instance = it}
            }
    }

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://api.openweathermap.org")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        service = retrofit.create(OpenWeatherApi::class.java)
    }

    fun getDetailedWeatherInfo(): Single<OpenWeatherCycleDataResponse>? = weatherCache.getCachedCities()



    fun getCities(): Single<OpenWeatherCycleDataResponse> {

        /*val cached = weatherCache.getCachedCities()
        if(cached!=null){
            return cached
        }

        service.getCities(API_KEY).enqueue(object : Callback<OpenWeatherCycleDataResponse> {
            override fun onResponse(call: Call<OpenWeatherCycleDataResponse>,
                                    response: Response<OpenWeatherCycleDataResponse>){
                val cities = response.body()?.list?.map {city ->
                    City(city.id,city.name,city.coord,city.weather,city.main,city.wind)
                }
                data.value = cities
            }
            override fun onFailure(call: Call<OpenWeatherCycleDataResponse>, t: Throwable) {
                System.out.println(t.message)
                //To change body of created functions use File | Settings | File Templates.
            }
        })*/

        val response = service.getCities(API_KEY)

        weatherCache.put(response)

        return response
    }


}