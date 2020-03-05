package com.lnicolet.currencyexchange

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.lnicolet.currencyexchange.Utils.atPosition
import com.lnicolet.currencyexchange.exchangelist.CurrencyExchangeActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.Thread.sleep


@RunWith(AndroidJUnit4::class)
class CurrencyExchangeActivityTest {
    @get:Rule
    var activityRule: ActivityTestRule<CurrencyExchangeActivity> =
        ActivityTestRule(
            CurrencyExchangeActivity::class.java,
            false,
            false
        )

    @Test
    fun verifyLoadingStatusIsInitiallyShown() {
        activityRule.launchActivity(Intent())
        onView(withId(R.id.loading)).check(matches(isDisplayed()))
    }

    @Test
    fun verifyRecyclerIsShownAfterAPIResponse() {
        activityRule.launchActivity(Intent())
        sleep(1500) // wait for API call
        onView(withId(R.id.recycler)).check(matches(isDisplayed()))
    }

    @Test
    fun verifyClickOnItemUpdateList() {
        activityRule.launchActivity(Intent())
        sleep(1500) // wait for API call
        onView(withId(R.id.recycler)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(1, click())
        )
        sleep(2000) // wait for API call and also re-ordering of list
        onView(withId(R.id.recycler)).check(
            matches(
                atPosition(0, hasDescendant(withText("AUD")))
            )
        )
    }
}