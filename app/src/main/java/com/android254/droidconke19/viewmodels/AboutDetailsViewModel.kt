package com.android254.droidconke19.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android254.droidconke19.datastates.Result
import com.android254.droidconke19.models.AboutDetailsModel
import com.android254.droidconke19.repository.AboutDetailsRepo
import com.android254.droidconke19.utils.NonNullMediatorLiveData
import kotlinx.coroutines.launch

class AboutDetailsViewModel(private val aboutDetailsRepo: AboutDetailsRepo) : ViewModel() {
    private val detailsStateMediatorLiveData = NonNullMediatorLiveData<List<AboutDetailsModel>>()
    private val detailsError = NonNullMediatorLiveData<String>()


    fun getAboutDetailsResponse(): LiveData<List<AboutDetailsModel>> = detailsStateMediatorLiveData

    fun getAboutDetailsError(): LiveData<String> = detailsError

    fun fetchAboutDetails(aboutType: String) {

        viewModelScope.launch {
            when (val value = aboutDetailsRepo.getAboutDetails(aboutType)) {
                is Result.Success -> detailsStateMediatorLiveData.postValue(value.data)
                is Result.Error -> detailsError.postValue(value.exception)
            }
        }
    }

}
