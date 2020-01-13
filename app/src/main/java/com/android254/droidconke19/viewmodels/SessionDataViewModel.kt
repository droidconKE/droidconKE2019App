package com.android254.droidconke19.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android254.droidconke19.datastates.FirebaseResult
import com.android254.droidconke19.models.SessionsUserFeedback
import com.android254.droidconke19.repository.RoomRepo
import com.android254.droidconke19.repository.SessionDataRepo
import com.android254.droidconke19.repository.SessionFeedbackRepo
import com.android254.droidconke19.repository.SpeakersRepo
import kotlinx.coroutines.launch


class SessionDataViewModel(private val sessionDataRepo: SessionDataRepo, private val speakersRepo: SpeakersRepo, private val roomRepo: RoomRepo, private val sessionFeedbackRepo: SessionFeedbackRepo) : ViewModel() {
    private val firebaseError = MediatorLiveData<String>()
    private val sessionFeedBackMediatorLiveData = MediatorLiveData<String>()


    fun getSessionFeedBackResponse(): LiveData<String> = sessionFeedBackMediatorLiveData

    fun getFirebaseError(): LiveData<String> = firebaseError


    fun sendSessionFeedBack(userEventFeedback: SessionsUserFeedback) {
        viewModelScope.launch {
            when (val value = sessionFeedbackRepo.sendFeedBack(userEventFeedback)) {
                is FirebaseResult.Success -> sessionFeedBackMediatorLiveData.postValue(value.data)
                is FirebaseResult.Error -> firebaseError.postValue(value.exception)
            }

        }
    }

}
