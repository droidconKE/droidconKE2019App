package com.android254.droidconke19.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android254.droidconke19.datastates.FirebaseResult
import com.android254.droidconke19.models.EventTypeModel
import com.android254.droidconke19.repository.EventTypeRepo
import com.android254.droidconke19.utils.NonNullMediatorLiveData
import kotlinx.coroutines.launch

class EventTypeViewModel( private val eventTypeRepo: EventTypeRepo) : ViewModel() {
    private val eventTypeModelMediatorLiveData = NonNullMediatorLiveData<List<EventTypeModel>>()
    private val eventDetailsError = NonNullMediatorLiveData<String>()


    fun getWifiDetailsResponse(): LiveData<List<EventTypeModel>> = eventTypeModelMediatorLiveData

    fun getWifiDetailsError(): LiveData<String> = eventDetailsError


    fun fetchSessions() {
        viewModelScope.launch {
            when (val value = eventTypeRepo.getSessionData()) {
                is FirebaseResult.Success -> eventTypeModelMediatorLiveData.postValue(value.data)
                is FirebaseResult.Error -> eventDetailsError.postValue(value.exception)
            }
        }

    }
}
