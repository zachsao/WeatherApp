package com.example.zach.weatherapp.utils


import com.example.zach.weatherapp.data.City
import com.example.zach.weatherapp.data.OpenWeatherCycleDataResponse
import com.example.zach.weatherapp.data.WeeklyForecast
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherApi {

    @GET("/data/2.5/find?cnt=5&units=metric")
    fun getCities(@Query("lat") latitude:Double,
                  @Query("lon") longitude:Double,
                  @Query("appid") app_id:String): Single<OpenWeatherCycleDataResponse>

    @GET("/data/2.5/weather?units=metric")
    fun getCityByName(@Query("q") name: String,
                      @Query("appid") app_id:String): Single<City>

    @GET("/data/2.5/forecast?&units=metric")
    fun get5DayForecast(@Query("id") cityId: Int,
                        @Query("appid") app_id: String): Single<List<WeeklyForecast>>
}