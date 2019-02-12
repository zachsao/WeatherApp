package com.example.zach.weatherapp.viewModel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.zach.weatherapp.data.City
import com.example.zach.weatherapp.data.ForecastRepository
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class ForecastViewModel @Inject constructor(private var forecastRepo: ForecastRepository ):ViewModel() {
    private var forecast = MutableLiveData<City>()
    private var disposable : CompositeDisposable? = CompositeDisposable()

    
    fun getDetailedWeatherInfo(cityId: Int): LiveData<City> {
        val cache = forecastRepo.getCache().getCitiesFromCache()
        cache?.forEach { city ->
            when (city.id) {
                cityId -> forecast.value = city
            }
        }


        return forecast
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.clear()
    }


}