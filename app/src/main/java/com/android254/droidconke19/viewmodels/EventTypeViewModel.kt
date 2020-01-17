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
    private val wifiDetailsMediatorLiveData = MediatorLiveData<WifiDetailsModel>()


    fun getEventTypeResponse(): LiveData<List<EventTypeModel>> = eventTypeModelMediatorLiveData

    fun getFirebaseError(): LiveData<String> = firebaseError

    fun getWifiDetailsReponse(): LiveData<WifiDetailsModel> = wifiDetailsMediatorLiveData


    fun fetchSessions() {
        viewModelScope.launch {
            when (val value = eventTypeRepo.getSessionData()) {
                is FirebaseResult.Success -> eventTypeModelMediatorLiveData.postValue(value.data)
                is FirebaseResult.Error -> firebaseError.postValue(value.exception)
            }
        }
    }

    fun fetchWifiDetails() {
        viewModelScope.launch(Dispatchers.IO) {
            when(val value = wifiDetailsRepo.fetchWifiDetails()){
                is FirebaseResult.Success -> wifiDetailsMediatorLiveData.postValue(value.data)
                is FirebaseResult.Error -> firebaseError.postValue(value.exception)
            }
        }
    }

}