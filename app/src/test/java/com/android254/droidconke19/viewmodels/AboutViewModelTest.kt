package com.android254.droidconke19.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android254.droidconke19.CoroutinesRule
import com.android254.droidconke19.observeOnce
import com.android254.droidconke19.repository.AboutDetailsRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import org.koin.test.mock.declareMock

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class AboutViewModelTest : KoinTest {

    private val aboutDetailsRepo: AboutDetailsRepo by inject()
    private val aboutViewModel: AboutViewModel by inject()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutinesRule = CoroutinesRule()

    @Before
    fun setup() {
        declareMock<AboutDetailsRepo>()
    }

    @Test
    fun `test fetchAboutDetails`() = runBlocking {

//        `when`(aboutDetailsRepo.getAboutDetails(any())).thenReturn(Result<List<AboutDetailsModel>>())

        aboutViewModel.fetchAboutDetails("value")

        aboutViewModel.getAboutDetailsResponse().observeOnce {
            Assert.assertTrue(it.isEmpty() ?: false)
        }

    }
}