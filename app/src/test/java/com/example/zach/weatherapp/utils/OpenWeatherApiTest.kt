package com.example.zach.weatherapp.utils

import com.example.zach.weatherapp.BuildConfig
import com.example.zach.weatherapp.data.OpenWeatherCycleDataResponse
import io.reactivex.observers.TestObserver
import junit.framework.Assert.assertEquals
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File

@RunWith(MockitoJUnitRunner::class)
class OpenWeatherApiTest {

    private lateinit var service: OpenWeatherApi

    private lateinit var mockWebServer: MockWebServer

    val apikey = BuildConfig.OPEN_WEATHER_MAP_API_KEY

    @Before
    fun createService() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        service = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(OpenWeatherApi::class.java)
    }

    @After
    fun stopService() {
        mockWebServer.shutdown()
    }

    @Test
    fun testGetCitiesReturnsOpenWeatherResponse(){

        val testObserver = TestObserver<OpenWeatherCycleDataResponse>()

        val path = "/data/2.5/find?cnt=5&units=metric&lat=50.36&lon=3.08&appid=$apikey"

        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody("{\"message\":\"accurate\",\n" +
                    "  \"cod\":\"200\",\n" +
                    "  \"count\":3,\n" +
                    "  \"list\":[{\"id\":2641549,\"name\":\"Newtonhill\",\"coord\":{\"lat\":57.0333,\"lon\":-2.15},\"main\":{\"temp\":275.15,\"pressure\":1010,\"humidity\":93,\"temp_min\":275.15,\"temp_max\":275.15},\"dt\":1521204600,\"wind\":{\"speed\":9.3,\"deg\":120,\"gust\":18},\"sys\":{\"country\":\"\"},\"rain\":null,\"snow\":null,\"clouds\":{\"all\":75},\"weather\":[{\"id\":311,\"main\":\"Drizzle\",\"description\":\"rain and drizzle\",\"icon\":\"09d\"}]},{\"id\":2636814,\"name\":\"Stonehaven\",\"coord\":{\"lat\":56.9637,\"lon\":-2.2118},\"main\":{\"temp\":275.15,\"pressure\":1010,\"humidity\":93,\"temp_min\":275.15,\"temp_max\":275.15},\"dt\":1521204600,\"wind\":{\"speed\":9.3,\"deg\":120,\"gust\":18},\"sys\":{\"country\":\"\"},\"rain\":null,\"snow\":null,\"clouds\":{\"all\":75},\"weather\":[{\"id\":311,\"main\":\"Drizzle\",\"description\":\"rain and drizzle\",\"icon\":\"09d\"}]},{\"id\":2640030,\"name\":\"Portlethen\",\"coord\":{\"lat\":57.0547,\"lon\":-2.1307},\"main\":{\"temp\":275.15,\"pressure\":1010,\"humidity\":93,\"temp_min\":275.15,\"temp_max\":275.15},\"dt\":1521204600,\"wind\":{\"speed\":9.3,\"deg\":120,\"gust\":18},\"sys\":{\"country\":\"\"},\"rain\":null,\"snow\":null,\"clouds\":{\"all\":75},\"weather\":[{\"id\":311,\"main\":\"Drizzle\",\"description\":\"rain and drizzle\",\"icon\":\"09d\"}]}]}\n")

        mockWebServer.enqueue(mockResponse)


        service.getCities(50.36,3.08,apikey).subscribe(testObserver)

        // No errors
        testObserver.assertNoErrors()
        // One openWeatherResponse emitted
        testObserver.assertValueCount(1)

        // Get the request that was just made
        val request = mockWebServer.takeRequest()
        // Make sure we made the request to the required path
        assertEquals(path,request.path)
    }

    @Test
    fun testGetCitiesReturnsError(){
        val testObserver = TestObserver<OpenWeatherCycleDataResponse>()

        val path = "/data/2.5/find?cnt=5&units=metric&lat=50.36&lon=3.08&appid=$apikey"

        val mockResponse = MockResponse()
            .setResponseCode(500) // Simulate a 500 HTTP Code

        // Enqueue request
        mockWebServer.enqueue(mockResponse)

        // Call the API
        service.getCities(50.36,3.08,apikey).subscribe(testObserver)

        // No values
        testObserver.assertNoValues()
        // One error recorded
        assertEquals(1, testObserver.errorCount())

        // Get the request that was just made
        val request = mockWebServer.takeRequest()
        // Make sure we made the request to the required path
        assertEquals(path, request.path)
    }

    /**
     * Helper function which will load JSON from
     * the path specified
     *
     * @param path : Path of JSON file
     * @return json : JSON from file at given path
     */
    fun getJson(path : String) : String {
        // Load the JSON response
        val uri = javaClass.classLoader.getResource(path) //Returns null for some reason...
        val file = File(uri.path)
        return String(file.readBytes())
    }
}