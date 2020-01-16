package com.android254.droidconke19.viewmodels

import androidx.lifecycle.*
import com.android254.droidconke19.datastates.FirebaseResult
import com.android254.droidconke19.models.EventTypeModel
import com.android254.droidconke19.models.WifiDetailsModel
import com.android254.droidconke19.repository.EventTypeRepo
import com.android254.droidconke19.repository.WifiDetailsRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EventTypeViewModel(
        private val eventTypeRepo: EventTypeRepo,
        private val wifiDetailsRepo: WifiDetailsRepo
) : ViewModel() {
    private val eventTypeModelMediatorLiveData = MediatorLiveData<List<EventTypeModel>>()
    private val firebaseError = MediatorLiveData<String>()

    val wifiDetails: MutableLiveData<FirebaseResult<WifiDetailsModel>> by lazy {
        MutableLiveData<FirebaseResult<WifiDetailsModel>>().also {
            fetchWifiDetails()
        }
    }

    fun getWifiDetailsResponse(): LiveData<List<EventTypeModel>> = eventTypeModelMediatorLiveData

    fun getFirebaseError(): LiveData<String> = firebaseError


    fun fetchSessions() {
        viewModelScope.launch {
            when (val value = eventTypeRepo.getSessionData()) {
                is FirebaseResult.Success -> eventTypeModelMediatorLiveData.postValue(value.data)
                is FirebaseResult.Error -> firebaseError.postValue(value.exception)
            }
        }
    }

    private fun fetchWifiDetails() {
        viewModelScope.launch(Dispatchers.IO) {
            wifiDetails.postValue(wifiDetailsRepo.fetchWifiDetails())
        }
    }

    fun retry() {
        if (firebaseError.value != null)
            fetchSessions()

        if (wifiDetails.value is FirebaseResult.Error)
            fetchWifiDetails()
    }
}