package com.android254.droidconke19.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android254.droidconke19.CoroutinesRule
import com.android254.droidconke19.datastates.FirebaseResult
import com.android254.droidconke19.observeOnce
import com.android254.droidconke19.repository.AboutDetailsRepo
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.empty
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class AboutViewModelTest {


    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutinesRule = CoroutinesRule()

    @MockK
    lateinit var aboutDetailsRepo: AboutDetailsRepo

    @InjectMockKs
    lateinit var aboutViewModel: AboutViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `test fetchAboutDetails`() {

        coEvery { aboutDetailsRepo.getAboutDetails(any()) } returns FirebaseResult.Success(emptyList())

        aboutViewModel.fetchAboutDetails("value")

        aboutViewModel.getAboutDetailsResponse().observeOnce {
            assertThat(it, `is`(empty()))
        }

    }

    @Test
    fun `test fetchAboutDetails error `() {

        coEvery { aboutDetailsRepo.getAboutDetails(any()) } returns FirebaseResult.Error("Some error")

        aboutViewModel.fetchAboutDetails("value")

        aboutViewModel.getAboutDetailsError().observeOnce {
            assertThat(it, `is`("Some error"))
        }

    }

    @Test
    fun `test getOrganizers`() {

        coEvery { aboutDetailsRepo.getAboutDetails(any()) } returns FirebaseResult.Success(emptyList())

        aboutViewModel.getOrganizers("value")

        aboutViewModel.getAboutDetailsResponse().observeOnce {
            assertThat(it, `is`(empty()))
        }

    }

    @Test
    fun `test getOrganizers error `() {

        coEvery { aboutDetailsRepo.getAboutDetails(any()) } returns FirebaseResult.Error("Some error")

        aboutViewModel.getOrganizers("value")

        aboutViewModel.getAboutDetailsError().observeOnce {
            assertThat(it, `is`("Some error"))
        }

    }

    @Test
    fun `test getSponsors`() {

        coEvery { aboutDetailsRepo.getAboutDetails(any()) } returns FirebaseResult.Success(emptyList())

        aboutViewModel.getSponsors("value")

        aboutViewModel.getAboutDetailsResponse().observeOnce {
            assertThat(it, `is`(empty()))
        }

    }

    @Test
    fun `test getSponsors error `() {

        coEvery { aboutDetailsRepo.getAboutDetails(any()) } returns FirebaseResult.Error("Some error")

        aboutViewModel.getSponsors("value")

        aboutViewModel.getAboutDetailsError().observeOnce {
            assertThat(it, `is`("Some error"))
        }

    }
}