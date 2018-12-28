package com.example.zach.weatherapp.data

data class City(var name:String,
                var id: Int,
                var lat: Number,
                var lng: Number,
                var forecast:Forecast) {
}