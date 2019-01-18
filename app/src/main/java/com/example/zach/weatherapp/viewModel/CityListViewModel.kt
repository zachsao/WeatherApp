package com.example.zach.weatherapp.viewModel

import android.util.Log
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


class CityListViewModel(private var forecastRepo: ForecastRepository):ViewModel() {
    private var cities : MutableLiveData<List<City>> = MutableLiveData()
    private var disposable : CompositeDisposable? = CompositeDisposable()

    companion object {
        private val TAG = "CityListViewModel"
    }

    fun getCities(): LiveData<List<City>> {
        disposable!!.add(forecastRepo.getCities().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableSingleObserver<OpenWeatherCycleDataResponse>(){
                override fun onSuccess(t: OpenWeatherCycleDataResponse) {
                    val data = t.list.map {city ->
                        City(city.id,city.name,city.coord,city.weather,city.main,city.wind)
                    }
                    cities.value = data
                }

                override fun onError(e: Throwable) {
                    Log.e(TAG, e.localizedMessage)
                }
            }))
        return cities
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.clear()
    }
}