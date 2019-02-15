package com.example.zach.weatherapp.viewModel

import com.example.zach.weatherapp.data.City
import com.example.zach.weatherapp.data.ForecastRepository
import com.example.zach.weatherapp.data.OpenWeatherCycleDataResponse
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CityListViewModelTest {
    @Mock
    private lateinit var repository: ForecastRepository

    @Mock

    private lateinit var viewModel: CityListViewModel


    @Before @Throws fun setUp(){
        RxAndroidPlugins.setInitMainThreadSchedulerHandler({Schedulers.trampoline()})

        MockitoAnnotations.initMocks(this)
        viewModel = CityListViewModel(repository)
    }

    @Test
    fun getCities() {
        val response = getMockedCities(5)

        `when`(repository.getCities(ArgumentMatchers.anyDouble(), ArgumentMatchers.anyDouble()))
            .thenReturn(Single.just(response))

        viewModel.getCities(0.0,0.0)

        verify(repository).getCache()

    }

    fun getMockedCities(count : Int) : OpenWeatherCycleDataResponse {
        val cities = ArrayList<City>()
        for (i in 0..count) {
            val city = mock(City::class.java)
            cities.add(city)
        }
        return OpenWeatherCycleDataResponse(cities)
    }
}