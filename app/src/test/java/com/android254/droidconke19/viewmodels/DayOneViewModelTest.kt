package com.android254.droidconke19.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android254.droidconke19.CoroutinesRule
import com.android254.droidconke19.repository.DayOneRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import org.koin.test.mock.declareMock

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class DayOneViewModelTest : KoinTest {

    private val dayOneRepo: DayOneRepo by inject()
    private val dayOneViewModel: DayOneViewModel by inject()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutinesRule = CoroutinesRule()

    @Before
    fun setup() {
        declareMock<DayOneRepo>()
    }

    @Test
    fun `test getDayOneSessions`() = runBlocking {
//        val state = SessionsState(emptyList())
//        `when`(dayOneRepo.getSessions()).thenReturn(MutableLiveData<SessionsState>().apply { value = state })
//
//        dayOneViewModel.getDayOneSessions()
//
//        dayOneViewModel.sessions.observeOnce {
//            Assert.assertTrue(it.sessionsModelList?.isEmpty() ?: false)
//        }

    }
}