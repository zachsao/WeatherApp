package com.example.zach.weatherapp.viewModel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.zach.weatherapp.data.City
import com.example.zach.weatherapp.data.ForecastRepository
import com.example.zach.weatherapp.data.WeekForecastResponse
import com.example.zach.weatherapp.data.WeeklyForecast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class ForecastViewModel @Inject constructor(private var forecastRepo: ForecastRepository ):ViewModel() {
    private var forecast = MutableLiveData<City>()
    private var disposable : CompositeDisposable? = CompositeDisposable()

    private var weeklyForecast = MutableLiveData<List<WeeklyForecast>>()
    
    fun getDetailedWeatherInfo(cityId: Int): LiveData<City> {
        val cache = forecastRepo.getCache().getCitiesFromCache()
        cache?.forEach { city ->
            when (city.id) {
                cityId -> forecast.value = city
            }
        }


        return forecast
    }

    fun getWeekForecast(cityId: Int): LiveData<List<WeeklyForecast>>{
        disposable!!.add(forecastRepo.get5DayForecast(cityId).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableSingleObserver<WeekForecastResponse>(){
                override fun onSuccess(t: WeekForecastResponse) {
                    weeklyForecast.value = t.list
                }
                override fun onError(e: Throwable) {
                    Timber.e(e.localizedMessage)
                }
            }))
        return weeklyForecast
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.clear()
    }


}