package com.example.zach.weatherapp.data

data class Forecast(var temp:Double,
                    var pressure:Int,
                    var humidity: Int,
                    var temp_min: Double,
                    var temp_max: Double,
                    var description: String?)