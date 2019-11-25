package com.android254.droidconke19.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android254.droidconke19.CoroutinesRule
import com.android254.droidconke19.datastates.FirebaseResult
import com.android254.droidconke19.repository.AboutDetailsRepo
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
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
    fun `test fetchAboutDetails`() = runBlockingTest {

        coEvery { aboutDetailsRepo.getAboutDetails(any()) } returns FirebaseResult.Success(emptyList())

        aboutViewModel.fetchAboutDetails("value", this)

        val response = aboutViewModel.getAboutDetailsResponse()

        assertThat(response.value, `is`(empty()))

    }

    @Test
    fun `test fetchAboutDetails error `() = runBlockingTest {

        coEvery { aboutDetailsRepo.getAboutDetails(any()) } returns FirebaseResult.Error("Some error")

        aboutViewModel.fetchAboutDetails("value", this)

        val error = aboutViewModel.getAboutDetailsError()

        assertThat(error.value, `is`("Some error"))

    }

    @Test
    fun `test getOrganizers`() = runBlockingTest {

        coEvery { aboutDetailsRepo.getAboutDetails(any()) } returns FirebaseResult.Success(emptyList())

        aboutViewModel.getOrganizers("value", this)

        val response = aboutViewModel.getOrganizersResponse()
        assertThat(response.value, `is`(empty()))
    }

    @Test
    fun `test getOrganizers error `() = runBlockingTest {

        coEvery { aboutDetailsRepo.getAboutDetails(any()) } returns FirebaseResult.Error("Some error")

        aboutViewModel.getOrganizers("value", this)

        val error = aboutViewModel.getOrganizerError()
        assertThat(error.value, `is`("Some error"))

    }

    @Test
    fun `test getSponsors`() = runBlockingTest {

        coEvery { aboutDetailsRepo.getAboutDetails(any()) } returns FirebaseResult.Success(emptyList())

        aboutViewModel.getSponsors("value", this)

        val response = aboutViewModel.getSponsorsResponse()
        assertThat(response.value, `is`(empty()))

    }

    @Test
    fun `test getSponsors error `() = runBlockingTest {

        coEvery { aboutDetailsRepo.getAboutDetails(any()) } returns FirebaseResult.Error("Some error")

        aboutViewModel.getSponsors("value", this)

        val error = aboutViewModel.getSponsorsError()

        assertThat(error.value, `is`("Some error"))

    }
}