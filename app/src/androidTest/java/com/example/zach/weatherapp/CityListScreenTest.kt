package com.example.zach.weatherapp

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.runner.AndroidJUnit4
import org.junit.Rule
import org.junit.runner.RunWith
import androidx.test.rule.ActivityTestRule
import com.example.zach.weatherapp.Adapter.CityAdapter
import com.example.zach.weatherapp.data.City
import org.junit.Test

@RunWith(AndroidJUnit4::class)
class CityListScreenTest {

    @get:Rule
    var activityRule: ActivityTestRule<MainActivity>
            = ActivityTestRule(MainActivity::class.java)

    @Test
    fun itemClickOpensDetailsFragment() {
        onView(withId(R.id.list))
            .perform(RecyclerViewActions.actionOnItemAtPosition<CityAdapter.ForecastViewHolder>(0,click()))
        onView(withId(R.id.temperature_textview)).check(matches(isDisplayed()))
    }
}