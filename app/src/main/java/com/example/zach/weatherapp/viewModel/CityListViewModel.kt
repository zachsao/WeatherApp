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
    //@Inject lateinit
    private var cities : MutableLiveData<List<City>> = MutableLiveData()
    //@Inject lateinit
     private var disposable : CompositeDisposable = CompositeDisposable()


    fun getCities(lat: Double,lon:Double): LiveData<List<City>> {
        disposable.add(forecastRepo.getCities(lat,lon).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {response ->
                    forecastRepo.getCache().saveCities(response.list)
                    cities.value = response.list
                },
                {throwable -> Timber.e(throwable)}
            )
        )
        return cities
    }

    fun updateLocation(lat: Double,lon:Double){
        disposable.add(forecastRepo.updateCitiesLocation(lat,lon).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {response ->
                    forecastRepo.getCache().saveCities(response.list)
                    cities.value = response.list
                },
                {throwable -> Timber.e(throwable)}
            )
        )
    }

    fun getCityByName(cityName: String): LiveData<City>{
        val searchedCity = MutableLiveData<City>()
        disposable.add(forecastRepo.getCityByName(cityName).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { city ->
                    searchedCity.value = city
                    forecastRepo.getCache().saveCities(listOf(city))
                },{throwable -> Timber.e(throwable) }
            )
        )
        return searchedCity
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}