package com.example.zach.weatherapp.data

data class WeeklyForecast(var city: City,
                          var forecastList: List<ForecastList>) {
}

data class ForecastList(var main: Forecast,
                        var weather: List<Weather>,
                        var date: String)