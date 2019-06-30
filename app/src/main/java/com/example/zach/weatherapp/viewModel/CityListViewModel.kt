package com.example.zach.weatherapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.zach.weatherapp.data.City
import com.example.zach.weatherapp.data.ForecastRepository
import com.example.zach.weatherapp.utils.LocationService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject


class CityListViewModel @Inject constructor(
    private val forecastRepo: ForecastRepository,
    private val locationService: LocationService
) : ViewModel() {
    private val cities: MutableLiveData<List<City>> = MutableLiveData()
    private val disposable: CompositeDisposable = CompositeDisposable()


    fun getCities(): LiveData<List<City>> = cities

    fun updateLocation(lat: Double, lon: Double) {
        forecastRepo.updateCitiesLocation(lat, lon).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    forecastRepo.getCache().saveCities(response.list)
                    cities.value = response.list
                },
                { throwable -> Timber.e(throwable) }
            )
            .addTo(disposable)

    }

    fun getCityByName(cityName: String): LiveData<City> {
        val searchedCity = MutableLiveData<City>()
        forecastRepo.getCityByName(cityName).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { city ->
                    searchedCity.value = city
                    forecastRepo.getCache().saveCities(listOf(city))
                }, { throwable -> Timber.e(throwable) }
            )
            .addTo(disposable)

        return searchedCity
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    fun listenToLocations() {

        locationService.registerToLocations()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                updateLocation(it.latitude, it.longitude)
            }, {
                Timber.e(it)

            })
            .addTo(disposable)
    }

    fun startLocation() {
        locationService.startLocationUpdates()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    Timber.i("Location started")
                }, {
                    Timber.e(it)
                }
            )
            .addTo(disposable)
    }

    fun stopLocation() {
        locationService.stopLocationUpdates()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    Timber.i("Location stopped")
                }, {
                    Timber.e(it)
                }
            )
            .addTo(disposable)
    }
}