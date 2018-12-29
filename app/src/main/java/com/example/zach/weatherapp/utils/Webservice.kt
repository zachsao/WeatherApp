package com.example.zach.weatherapp.utils

import com.example.zach.weatherapp.data.City
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface Webservice {

    @GET("/data/2.5/find?lat=55.5&lon=37.5&cnt=5")
    fun getCities(): Call<List<City>>
}