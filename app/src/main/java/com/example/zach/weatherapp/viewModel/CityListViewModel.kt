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
    private var cities : MutableLiveData<List<City>> = MutableLiveData()
    private var disposable : CompositeDisposable? = CompositeDisposable()


    fun getCities(lat: Double,lon:Double): LiveData<List<City>> {
        disposable!!.add(forecastRepo.getCities(lat,lon).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableSingleObserver<OpenWeatherCycleDataResponse>(){
                override fun onSuccess(t: OpenWeatherCycleDataResponse) {
                    val data = t.list.map {city ->
                        City(city.id,city.name,city.coord,city.weather,city.main,city.wind)
                    }
                    cities.value = data
                }

                override fun onError(e: Throwable) {
                    Timber.e(e.localizedMessage)
                }
            }))
        return cities
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.clear()
    }
}