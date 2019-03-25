package com.example.zach.weatherapp.data

data class WeeklyForecast(var main: Forecast,
                          var weather: List<Weather>,
                          var dt_txt: String){

    var expanded: Boolean = false

}

data class WeekForecastResponse(var list: List<WeeklyForecast>)