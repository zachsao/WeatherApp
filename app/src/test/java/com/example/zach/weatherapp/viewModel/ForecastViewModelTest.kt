package com.example.zach.weatherapp.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.zach.weatherapp.data.*
import com.example.zach.weatherapp.utils.WeatherCache
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import org.mockito.ArgumentMatchers
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.Mockito.*

class ForecastViewModelTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: ForecastRepository

    @InjectMocks
    private lateinit var viewModel: ForecastViewModel

    @Mock
    lateinit var cache: WeatherCache

    private val city = City(0,"", Coordinates(0.0,0.0),listOf(Weather(0,"","","")),
        Forecast(0.0,0.0,0,0.0,0.0), Wind(0.0,0.0)
    )

    @Before
    fun setUp() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler({ Schedulers.trampoline()})
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }

        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun getForecast() {
    }

    @Test
    fun getDetailedWeatherInfo() {
    }

    @Test
    fun getWeekForecast_returns3pmForecast() {
        val expected_result = listOf(WeeklyForecast(Forecast(0.0,0.0,0,0.0,0.0),
            listOf(Weather(0,"","","")),"15:00"))

        val shouldnt_be_in_result = WeeklyForecast(Forecast(0.0,0.0,0,0.0,0.0),
            listOf(Weather(0,"","","")),"")

        val response = WeekForecastResponse(listOf(expected_result[0],shouldnt_be_in_result))
        `when`(repository.get5DayForecast(ArgumentMatchers.anyInt())).thenReturn(Single.just(response))

        assertEquals(viewModel.getWeekForecast(0).value,expected_result)
    }

}