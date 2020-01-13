package com.android254.droidconke19.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android254.droidconke19.datastates.FirebaseResult
import com.android254.droidconke19.models.UserEventFeedback
import com.android254.droidconke19.repository.EventFeedbackRepo
import kotlinx.coroutines.launch


class FeedBackViewModel(private val eventFeedBackRepo: EventFeedbackRepo) : ViewModel() {
    private val feedBackStateMediatorLiveData = MediatorLiveData<String>()
    private val firebaseError = MediatorLiveData<String>()

    fun getEventFeedBackResponse(): LiveData<String> = feedBackStateMediatorLiveData

    fun getFirebaseError(): LiveData<String> = firebaseError


    fun sendEventFeedBack(userEventFeedback: UserEventFeedback) {
        viewModelScope.launch {
            when (val value = eventFeedBackRepo.sendFeedBack(userEventFeedback)) {
                is FirebaseResult.Success -> feedBackStateMediatorLiveData.postValue(value.data)
                is FirebaseResult.Error -> firebaseError.postValue(value.exception)
            }
        }
    }
}