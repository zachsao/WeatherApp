package com.example.zach.weatherapp.data

data class WeeklyForecast(var main: Forecast,
                          var weather: List<Weather>,
                          var date: String)