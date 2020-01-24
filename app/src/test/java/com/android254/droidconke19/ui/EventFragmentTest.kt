package com.android254.droidconke19.ui

import android.os.Build
import android.view.View
import android.widget.ImageView
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.android254.droidconke19.R
import com.android254.droidconke19.models.EventTypeModel
import com.android254.droidconke19.models.WifiDetailsModel
import com.android254.droidconke19.ui.events.EventFragment
import com.android254.droidconke19.viewmodels.EventTypeViewModel
import io.mockk.every
import io.mockk.mockk
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.koin.test.inject
import org.robolectric.annotation.Config
import org.robolectric.annotation.LooperMode
import org.robolectric.shadows.ShadowLog


@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
@LooperMode(LooperMode.Mode.PAUSED)
class EventFragmentTest : AutoCloseKoinTest() {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val eventTypeViewModel: EventTypeViewModel by inject()

    @Before
    fun init() {
        ShadowLog.stream = System.out

        loadKoinModules(module {
            single(override = true) { mockk<EventTypeViewModel>(relaxUnitFun = true, relaxed = true) }
        })
    }

    val eventTypeList_FakeData = listOf(
            EventTypeModel("", "description 1"),
            EventTypeModel("", "description 2")
    )

    @Test
    fun `test progressBar displayed before event-types list received`() {
        // Simulate situation before UI receives event-types list
        every { eventTypeViewModel.getEventTypeResponse() } answers {
            // LiveData without a value for event-types list
            MutableLiveData<List<EventTypeModel>>()
        }

        launchFragmentInContainer<EventFragment>(themeResId = R.style.AppTheme_Launch)

        onView(withId(R.id.progressBar)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    @Test
    fun `test progressBar hidden when event-types list received`() {
        // Simulate situation after UI receives event-types list
        every { eventTypeViewModel.getEventTypeResponse() } answers {
            MutableLiveData<List<EventTypeModel>>().also {
                it.value = eventTypeList_FakeData
            }
        }

        launchFragmentInContainer<EventFragment>(themeResId = R.style.AppTheme_Launch)

        onView(withId(R.id.progressBar)).check(matches(withEffectiveVisibility(Visibility.GONE)))
    }

    @Test
    fun `test recyclerview populated when event-types list received`() {
        every { eventTypeViewModel.getEventTypeResponse() } answers {
            MutableLiveData<List<EventTypeModel>>().also {
                it.value = eventTypeList_FakeData
            }
        }

        launchFragmentInContainer<EventFragment>(themeResId = R.style.AppTheme_Launch)

        onView(withId(R.id.eventTypesRv))
                .check(withItemCount(eventTypeList_FakeData.size))
    }

    @Test
    fun `test recyclerView item has description and drawable`() {
        every { eventTypeViewModel.getEventTypeResponse() } answers {
            MutableLiveData<List<EventTypeModel>>().also {
                it.value = eventTypeList_FakeData
            }
        }

        launchFragmentInContainer<EventFragment>(themeResId = R.style.AppTheme_Launch)

        eventTypeList_FakeData.forEachIndexed { index, eventType ->
            onView(withId(R.id.eventTypesRv))
                    .check(matches(atPosition(index, allOf(
                            hasDescendant(withText(eventType.description)), hasDescendant(hasDrawable())))))
        }
    }

    @Test
    fun `test error-SnackBar displayed when event-types list fetch unsuccessful`() {
        val error = "Error while fetching"
        every { eventTypeViewModel.getFirebaseError() } answers {
            MutableLiveData<String>().also {
                it.value = "Error while fetching"
            }
        }

        launchFragmentInContainer<EventFragment>(themeResId = R.style.AppTheme_Launch)

        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText(error)))
    }

    @Test
    fun `test wifi details displayed when fetch successful`() {
        val wifiDetailsModel = WifiDetailsModel(
                wifiSsid = "WifiSSID", wifiPassword = "WifiPassword")

        // Given EventTypeViewModel's wifiDetails livedata value is
        // set to the wifiDetailsModel above
        every { eventTypeViewModel.getWifiDetailsReponse() } answers {
            MutableLiveData<WifiDetailsModel>().also {
                it.setValue(wifiDetailsModel)
            }
        }

        // When EventFragment is launched
        launchFragmentInContainer<EventFragment>(themeResId = R.style.AppTheme_Launch)

        // Then ssid and password in wifiDetailsModel above are displayed on UI
        onView(withId(R.id.wifiSsidText)).check(matches(withText("WifiSSID")))
        onView(withId(R.id.wifiPasswordText)).check(matches(withText("WifiPassword")))
    }

    @Test
    fun `test error-SnackBar displayed when wifiDetails fetch unsuccessful`() {
        // Given there is an error in fetching wifi details
        val error = "Exception in fetching"
        every { eventTypeViewModel.getFirebaseError() } answers {
            MutableLiveData<String>().also {
                it.setValue(error)
            }
        }

        // When EventFragment is launched
        launchFragmentInContainer<EventFragment>(themeResId = R.style.AppTheme_Launch)

        // Then Snackbar with error message is displayed
        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText(error)))

    }

}

private fun withItemCount(expectedCount: Int): ViewAssertion {
    return ViewAssertion { view, noViewFoundException ->
        if (noViewFoundException != null)
            throw noViewFoundException

        assertThat((view as RecyclerView).adapter?.itemCount, `is`(expectedCount))
    }
}

private fun atPosition(position: Int, matcher: Matcher<View>): Matcher<View> {
    return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
        override fun describeTo(description: Description?) {
            description?.appendText("has item at position $position: ")
            matcher.describeTo(description)
        }

        override fun matchesSafely(item: RecyclerView?): Boolean {
            val viewHolder = item?.findViewHolderForAdapterPosition(position) ?: return false
            return matcher.matches(viewHolder.itemView)
        }
    }
}

private fun hasDrawable(): Matcher<View> {
    return object : BoundedMatcher<View, ImageView>(ImageView::class.java) {
        override fun describeTo(description: Description?) {
            description?.appendText("has drawable")
        }

        override fun matchesSafely(item: ImageView?): Boolean {
            return item?.drawable != null
        }
    }
}