package com.example.zach.weatherapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.zach.weatherapp.data.City
import androidx.lifecycle.MutableLiveData
import com.example.zach.weatherapp.data.ForecastRepository
import com.example.zach.weatherapp.data.OpenWeatherCycleDataResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject


class CityListViewModel @Inject constructor(private var forecastRepo: ForecastRepository):ViewModel() {
    @Inject lateinit var cities : MutableLiveData<List<City>>
    @Inject lateinit var disposable : CompositeDisposable


    fun getCities(lat: Double,lon:Double): LiveData<List<City>> {
        disposable.add(forecastRepo.getCities(lat,lon).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableSingleObserver<OpenWeatherCycleDataResponse>(){
                override fun onSuccess(t: OpenWeatherCycleDataResponse) {
                    val data = t.list.map {city ->
                        City(city.id,city.name,city.coord,city.weather,city.main,city.wind)
                    }
                    forecastRepo.getCache().saveCities(t.list)
                    cities.value = data
                }

                override fun onError(e: Throwable) {
                    Timber.e(e.localizedMessage)
                }
            }))
        return cities
    }

    fun getCityByName(cityName: String): LiveData<City>{
        val searchedCity = MutableLiveData<City>()
        disposable.add(forecastRepo.getCityByName(cityName).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableSingleObserver<City>(){
                override fun onSuccess(t: City) {

                    searchedCity.value = t
                    forecastRepo.getCache().saveCities(listOf(t))
                }

                override fun onError(e: Throwable) {
                    Timber.e(e.localizedMessage)
                }
            }))
        return searchedCity
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}