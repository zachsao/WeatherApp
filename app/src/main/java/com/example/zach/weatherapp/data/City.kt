package com.example.zach.weatherapp.data

data class City(var id: Int,
                var name: String,
                var coord : Coordinates,
                var weather: List<Weather>,
                var main :Forecast)

data class Coordinates(var lat: Double, var lon: Double)

data class OpenWeatherCycleDataResponse(var list: List<City>)

data class Weather(var id:Int, var main:String, var description:String, var icon:String)