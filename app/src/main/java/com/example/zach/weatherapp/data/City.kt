package com.example.zach.weatherapp.data

data class City(var id: Int,
                var name: String,
                var coord : Coordinates,
                var main :Forecast)

data class Coordinates(var lat: Double, var lon: Double)

data class OpenWeatherCycleDataResponse(var list: List<City>)