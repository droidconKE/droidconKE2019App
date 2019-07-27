package com.android254.droidconke19.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android254.droidconke19.CoroutinesRule
import com.android254.droidconke19.repository.SessionsRepo
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
class SessionsViewModelTest : KoinTest {

    private val sessionsRepo: SessionsRepo by inject()
    private val sessionsViewModel: SessionsViewModel by inject()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutinesRule = CoroutinesRule()

    @Before
    fun setup() {
        declareMock<SessionsRepo>()
    }

    @Test
    fun `test getDayOneSessions`() = runBlocking {
//        val state = SessionsState(emptyList())
//        `when`(sessionsRepo.getSessions()).thenReturn(MutableLiveData<SessionsState>().apply { value = state })
//
//        sessionsViewModel.getSessions()
//
//        sessionsViewModel.sessions.observeOnce {
//            Assert.assertTrue(it.sessionsModelList?.isEmpty() ?: false)
//        }

    }
}