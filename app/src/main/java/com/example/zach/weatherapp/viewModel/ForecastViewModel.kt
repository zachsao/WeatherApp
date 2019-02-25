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
    //@Inject lateinit
    var forecast = MutableLiveData<City>()

    //@Inject lateinit
    var disposable = CompositeDisposable()

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
        disposable.add(forecastRepo.get5DayForecast(cityId).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableSingleObserver<WeekForecastResponse>(){
                override fun onSuccess(t: WeekForecastResponse) {
                    //filter the result list with the 3pm forecast to get overall max temperature
                    val three_pm_forecast_list = t.list.filter { it.dt_txt.contains("15:00") }
                    //filter the result list with the 6am forecast to get overall min temperature
                    val six_am_forecast_list = t.list.filter { it.dt_txt.contains("06:00")}
                    //store the 6am min_temp values in the 3pm min_temp properties
                    for(i in six_am_forecast_list.indices){
                        three_pm_forecast_list[i].main.temp_min = six_am_forecast_list[i].main.temp_min
                    }
                    //set the liveData value to the updated 3pm list
                    weeklyForecast.value = three_pm_forecast_list


                }
                override fun onError(e: Throwable) {
                    Timber.e(e.localizedMessage)
                }
            }))
        return weeklyForecast
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}
