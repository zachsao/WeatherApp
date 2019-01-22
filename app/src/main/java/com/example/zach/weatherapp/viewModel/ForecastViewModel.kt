package com.example.zach.weatherapp.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.zach.weatherapp.data.City
import com.example.zach.weatherapp.data.Forecast
import com.example.zach.weatherapp.data.ForecastRepository
import com.example.zach.weatherapp.data.OpenWeatherCycleDataResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class ForecastViewModel(private var forecastRepo: ForecastRepository ):ViewModel() {
    private var forecast = MutableLiveData<City>()
    private var disposable : CompositeDisposable? = CompositeDisposable()

    companion object {
        private val TAG = "ForecastViewModel"
    }

    fun init(cityId: Int) {
        val cache = forecastRepo.getDetailedWeatherInfo()
        if(cache != null){
            disposable!!.add(cache.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableSingleObserver<OpenWeatherCycleDataResponse>(){
                    override fun onSuccess(t: OpenWeatherCycleDataResponse) {
                        t.list.forEach { city ->
                            when(city.id){
                                cityId -> forecast.value = city
                            }
                        }
                    }

                    override fun onError(e: Throwable) {
                        Log.e(TAG, e.localizedMessage)
                    }
                }))
        }

    }


    fun getDetailedWeatherInfo(): LiveData<City> {
        return forecast
    }
}