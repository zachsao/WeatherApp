package com.example.zach.weatherapp.data

data class ForecastViewItem(
    val name: String,
    val description: String,
    val temp: Int,
    val temp_max: Int,
    val temp_min: Int,
    val pressure: Double,
    val wind_speed: Double,
    val humidity: Int,
    val icon: String
)