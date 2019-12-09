package com.android254.droidconke19.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import com.android254.droidconke19.CoroutinesRule
import com.android254.droidconke19.datastates.FirebaseResult
import com.android254.droidconke19.getOrAwaitValue
import com.android254.droidconke19.models.WifiDetailsModel
import com.android254.droidconke19.repository.EventTypeRepo
import com.android254.droidconke19.repository.WifiDetailsRepo
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class EventTypeViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @ObsoleteCoroutinesApi
    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutinesRule = CoroutinesRule()

    @MockK
    lateinit var eventTypeRepo: EventTypeRepo
    @MockK
    lateinit var wifiDetailsRepo: WifiDetailsRepo

    @InjectMockKs
    lateinit var eventTypeViewModel: EventTypeViewModel

    @Before
    fun initMockk() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `test wifiDetails on successful wifi details fetch`() {
        val wifiDetailsModel = WifiDetailsModel(
                "WifiSsid", "WifiPassword")

        //Given fetching wifi details returns following result
        coEvery { wifiDetailsRepo.fetchWifiDetails() } answers {
            FirebaseResult.Success(wifiDetailsModel)
        }

        //When lazily-initialized wifiDetails livedata accessed
        val wifiDetails = eventTypeViewModel.wifiDetails

        //Then wifiDetails livedata value is
        assertThat<FirebaseResult<WifiDetailsModel>>(
                wifiDetails.getOrAwaitValue(), `is`(FirebaseResult.Success(wifiDetailsModel)))
    }

    @Test
    fun `test wifiDetails on unsuccessful wifi details fetch`() {
        //Given fetching wifi details returns failure
        coEvery { wifiDetailsRepo.fetchWifiDetails() } answers {
            FirebaseResult.Error("Failure fetching wifi details")
        }

        //When ViewModel's lazily-initialized wifiDetails livedata accessed
        val wifiDetailsLiveData = eventTypeViewModel.wifiDetails

        //Then wifiDetails livedata value is
        assertThat<FirebaseResult<WifiDetailsModel>>(
                wifiDetailsLiveData.getOrAwaitValue(),
                `is`(FirebaseResult.Error("Failure fetching wifi details"))
        )
    }

    @Test
    fun `test retry when error in fetching event-types list only`() {
        // GIVEN Only event-types list fetch is unsuccessful
        coEvery { eventTypeRepo.getSessionData() } answers {
            FirebaseResult.Error("Exception Message")
        }
        coEvery { wifiDetailsRepo.fetchWifiDetails() } answers {
            FirebaseResult.Success(mockk())
        }
        eventTypeViewModel.fetchSessions()
        eventTypeViewModel.wifiDetails

        // WHEN retry is called
        eventTypeViewModel.retry()

        // THEN Only the event-types list is re-fetched
        coVerify(exactly = 2) { eventTypeRepo.getSessionData() }
        coVerify(exactly = 1) { wifiDetailsRepo.fetchWifiDetails() }
    }

    @Test
    fun `test retry when error in fetching wifi details only`() {
        // GIVEN Only wifi details fetch is unsuccessful
        coEvery { eventTypeRepo.getSessionData() } answers {
            FirebaseResult.Success(mockk())
        }
        coEvery { wifiDetailsRepo.fetchWifiDetails() } answers {
            FirebaseResult.Error("Exception message")
        }
        eventTypeViewModel.fetchSessions()
        eventTypeViewModel.wifiDetails.getOrAwaitValue()  // Await for value to be set

        // WHEN retry is called
        eventTypeViewModel.retry()

        // THEN Only the wifi details are re-fetched
        coVerify(exactly = 1) { eventTypeRepo.getSessionData() }
        coVerify(exactly = 2) { wifiDetailsRepo.fetchWifiDetails() }
    }

    @Test
    fun `test retry when error in fetching both event-types list and wifi details`() {
        // GIVEN Both event-types list and wifi details fetch are unsuccessful
        coEvery { eventTypeRepo.getSessionData() } answers {
            FirebaseResult.Error("Exception message")
        }
        coEvery { wifiDetailsRepo.fetchWifiDetails() } answers {
            FirebaseResult.Error("Exception message")
        }
        eventTypeViewModel.fetchSessions()
        eventTypeViewModel.wifiDetails.getOrAwaitValue()  // Await for value to be set

        // WHEN retry is called
        eventTypeViewModel.retry()


        // THEN Only the wifi details are re-fetched
        coVerify(exactly = 2) { eventTypeRepo.getSessionData() }
        coVerify(exactly = 2) { wifiDetailsRepo.fetchWifiDetails() }
    }
}