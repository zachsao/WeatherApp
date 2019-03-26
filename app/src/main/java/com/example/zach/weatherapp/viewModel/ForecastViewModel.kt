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
import java.util.*
import javax.inject.Inject

class ForecastViewModel @Inject constructor(private var forecastRepo: ForecastRepository ):ViewModel() {
    //@Inject lateinit
    var forecast = MutableLiveData<City>()

    //@Inject lateinit

    var disposable = CompositeDisposable()

    private var weeklyForecast = MutableLiveData<Array<MutableList<WeeklyForecast>>>()
    
    fun getDetailedWeatherInfo(cityId: Int): LiveData<City> {
        val cache = forecastRepo.getCache().getCitiesFromCache()
        cache?.forEach { city ->
            when (city.id) {
                cityId -> forecast.value = city
            }
        }


        return forecast
    }

    fun getWeekForecast(cityId: Int): MutableLiveData<Array<MutableList<WeeklyForecast>>> {
        disposable.add(forecastRepo.get5DayForecast(cityId).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableSingleObserver<WeekForecastResponse>(){
                override fun onSuccess(t: WeekForecastResponse) {
                    weeklyForecast.value = organizeList(5,t.list)

                }
                override fun onError(e: Throwable) {
                    Timber.e(e.localizedMessage)
                }
            }))
        return weeklyForecast
    }

    fun organizeList(length: Int,L: List<WeeklyForecast>): Array<MutableList<WeeklyForecast>> {
        val days = Array(length, {mutableListOf<WeeklyForecast>()})

        days[0].add(L[0])
        var k = 1
        for(i in 0..days.size){
            while(k<L.size && L[k].dt_txt.subSequence(0,10).equals(L[k-1].dt_txt.subSequence(0,10))){
                days[i].add(L[k])
                k++
            }
            if(i<days.size-1 && k<days.size)
                days[i+1].add(L[k])
            k++
        }
        return days
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}
