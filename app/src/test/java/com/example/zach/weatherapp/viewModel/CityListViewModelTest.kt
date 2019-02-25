package com.example.zach.weatherapp.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.zach.weatherapp.data.*
import com.example.zach.weatherapp.utils.WeatherCache
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
    lateinit var cache: WeatherCache

    private val city = City(0,"", Coordinates(0.0,0.0),listOf(Weather(0,"","","")),
        Forecast(0.0,0.0,0,0.0,0.0), Wind(0.0,0.0))

    @Before @Throws fun setUp(){
        RxAndroidPlugins.setInitMainThreadSchedulerHandler({Schedulers.trampoline()})
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }

        MockitoAnnotations.initMocks(this)

    }

    @Test
    fun getCities() {
        val response = OpenWeatherCycleDataResponse(listOf(city))

        `when`(repository.getCities(ArgumentMatchers.anyDouble(), ArgumentMatchers.anyDouble()))
            .thenReturn(Single.just(response))
        `when`(repository.getCache()).thenReturn(cache)

        val result = viewModel.getCities(0.0,0.0)

        verify(repository).getCities(0.0,0.0)
        verify(repository).getCache() //should be called but isn't

        
        assertEquals(response.list,result.value) //result.value should be a list of 5 mocked cities but is null

    }

    @Test
    fun getCityByNameTest(){

        `when`(repository.getCityByName(ArgumentMatchers.anyString()))
            .thenReturn(Single.just(city))

        `when`(repository.getCache()).thenReturn(cache)

        val result = viewModel.getCityByName("")

        verify(repository).getCityByName("")
        verify(repository).getCache()
        assertEquals(result.value,city)
    }
}