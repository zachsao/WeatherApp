package com.example.zach.weatherapp.viewModel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.zach.weatherapp.data.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class ForecastViewModel @Inject constructor(private var forecastRepo: ForecastRepository) : ViewModel() {
    var forecast = MutableLiveData<ForecastViewItem>()
    var disposable = CompositeDisposable()

    private var weeklyForecast = MutableLiveData<List<List<WeeklyForecast>>>()

    fun getDetailedWeatherInfo(cityId: Int): LiveData<ForecastViewItem> {
        val cache = forecastRepo.getCache().getCitiesFromCache()
        cache?.forEach { city ->
            when (city.id) {
                cityId -> forecast.value = modelToItemView(city)
            }
        }
        return forecast
    }

    fun getWeekForecast(cityId: Int): MutableLiveData<List<List<WeeklyForecast>>> {
        disposable.add(forecastRepo.get5DayForecast(cityId).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<WeekForecastResponse>() {
                override fun onSuccess(t: WeekForecastResponse) {
                    weeklyForecast.value = t.list.groupBy { it.dt_txt.substring(0, 10) }.map { it.value }
                }
                override fun onError(e: Throwable) {
                    Timber.e(e.localizedMessage)
                }
            })
        )
        return weeklyForecast
    }

    private fun modelToItemView(city: City): ForecastViewItem{
        return ForecastViewItem(
            city.name,
            city.weather[0].description,
            city.main.temp.toInt(),
            city.main.temp_max.toInt(),
            city.main.temp_min.toInt(),
            city.main.pressure,
            city.wind.speed.toInt()*3.6,
            city.main.humidity,
            "http://openweathermap.org/img/wn/${city.weather[0].icon}@2x.png"
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}
