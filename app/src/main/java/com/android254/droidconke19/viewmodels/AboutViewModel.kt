package com.android254.droidconke19.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android254.droidconke19.datastates.FirebaseResult
import com.android254.droidconke19.models.AboutDetailsModel
import com.android254.droidconke19.repository.AboutDetailsRepo
import kotlinx.coroutines.launch

class AboutViewModel(private val aboutDetailsRepo: AboutDetailsRepo) : ViewModel() {
    private val detailsStateMediatorLiveData = MediatorLiveData<List<AboutDetailsModel>>()
    private val firebaseError = MediatorLiveData<String>()
    private val organizersMediatorLiveData = MediatorLiveData<List<AboutDetailsModel>>()
    private val sponsorsMediatorLiveData = MediatorLiveData<List<AboutDetailsModel>>()


    fun getAboutDetailsResponse(): LiveData<List<AboutDetailsModel>> = detailsStateMediatorLiveData

    fun getFirebaseError(): LiveData<String> = firebaseError

    fun getOrganizersResponse(): LiveData<List<AboutDetailsModel>> = organizersMediatorLiveData

    fun getSponsorsResponse(): LiveData<List<AboutDetailsModel>> = sponsorsMediatorLiveData


    fun fetchAboutDetails(aboutType: String) {
        viewModelScope.launch {
            when (val value = aboutDetailsRepo.getAboutDetails(aboutType)) {
                is FirebaseResult.Success -> detailsStateMediatorLiveData.postValue(value.data)
                is FirebaseResult.Error -> firebaseError.postValue(value.exception)
            }
        }
    }

    fun getOrganizers(aboutType: String) {
        viewModelScope.launch {
            when (val value = aboutDetailsRepo.getAboutDetails(aboutType)) {
                is FirebaseResult.Success -> organizersMediatorLiveData.postValue(value.data)
                is FirebaseResult.Error -> firebaseError.postValue(value.exception)
            }
        }

    }

    fun getSponsors(aboutType: String) {
        viewModelScope.launch {
            when (val value = aboutDetailsRepo.getAboutDetails(aboutType)) {
                is FirebaseResult.Success -> sponsorsMediatorLiveData.postValue(value.data)
                is FirebaseResult.Error -> firebaseError.postValue(value.exception)
            }
        }
    }

}
