package com.android254.droidconke19.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android254.droidconke19.datastates.FirebaseResult
import com.android254.droidconke19.models.TravelInfoModel
import com.android254.droidconke19.repository.TravelDetailsRepo
import kotlinx.coroutines.launch

class TravelViewModel(private val travelDetailsRepo: TravelDetailsRepo) : ViewModel() {
    private val travelDetailsMediatorLiveData = MediatorLiveData<TravelInfoModel>()
    private val firebaseError = MediatorLiveData<String>()

    fun getFirebaseError(): LiveData<String> = firebaseError

    fun getTravelDetailsResponse(): LiveData<TravelInfoModel> = travelDetailsMediatorLiveData

    fun getTravelDetails() {
        viewModelScope.launch {
            when (val value = travelDetailsRepo.fetchTravelDetails()) {
                is FirebaseResult.Success -> travelDetailsMediatorLiveData.postValue(value.data)
                is FirebaseResult.Error -> firebaseError.postValue(value.exception)
            }
        }
    }
}