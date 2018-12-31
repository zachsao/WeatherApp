package com.example.zach.weatherapp.utils


import com.example.zach.weatherapp.data.OpenWeatherCycleDataResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OpenWeatherApi {

    @GET("/data/2.5/find?lat=55.5&lon=37.5&cnt=5&units=metric")
    fun getCities(@Query("appid") app_id:String): Call<OpenWeatherCycleDataResponse>
}