package com.example.zach.weatherapp.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.zach.weatherapp.data.City
import com.example.zach.weatherapp.data.ForecastRepository
import com.example.zach.weatherapp.data.OpenWeatherCycleDataResponse
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import io.reactivex.plugins.RxJavaPlugins


@RunWith(JUnit4::class)
class CityListViewModelTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: ForecastRepository

    @InjectMocks
    private lateinit var viewModel: CityListViewModel

    @Mock
    lateinit var observer: Observer<List<City>>


    @Before @Throws fun setUp(){
        RxAndroidPlugins.setInitMainThreadSchedulerHandler({Schedulers.trampoline()})
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }

        MockitoAnnotations.initMocks(this)

    }

    @Test
    fun getCities() {
        val response = getMockedCities(5)

        `when`(repository.getCities(ArgumentMatchers.anyDouble(), ArgumentMatchers.anyDouble()))
            .thenReturn(Single.just(response))

        viewModel.getCities(0.0,0.0).observeForever(observer)

        verify(repository).getCities(0.0,0.0)
        verify(repository).getCache() //should be called but isn't
        verify(observer).onChanged(response.list)
        
        //assertEquals(response.list,result.value) //result.value should be a list of 5 mocked cities but is null

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