package com.android254.droidconke19.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android254.droidconke19.datastates.FirebaseResult
import com.android254.droidconke19.models.UserEventFeedback
import com.android254.droidconke19.repository.EventFeedbackRepo
import com.android254.droidconke19.utils.NonNullMediatorLiveData
import kotlinx.coroutines.launch


class FeedBackViewModel(private val eventFeedBackRepo : EventFeedbackRepo) : ViewModel() {
    private val feedBackStateMediatorLiveData = NonNullMediatorLiveData<String>()
    private val eventFeedbackError = NonNullMediatorLiveData<String>()

    fun getEventFeedBackResponse(): LiveData<String> = feedBackStateMediatorLiveData

    fun getEventFeedbackError(): LiveData<String> = eventFeedbackError


    fun sendEventFeedBack(userEventFeedback: UserEventFeedback) {
        viewModelScope.launch {
            when (val value = eventFeedBackRepo.sendFeedBack(userEventFeedback)) {
                is FirebaseResult.Success -> feedBackStateMediatorLiveData.postValue(value.data)
                is FirebaseResult.Error -> eventFeedbackError.postValue(value.exception)
            }
        }
    }
}