package com.android254.droidconke19.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android254.droidconke19.datastates.Result
import com.android254.droidconke19.models.*
import com.android254.droidconke19.repository.*
import com.android254.droidconke19.utils.NonNullMediatorLiveData
import kotlinx.coroutines.launch


class SessionDataViewModel(private val sessionDataRepo: SessionDataRepo, private val speakersRepo: SpeakersRepo, private val roomRepo: RoomRepo, private val sessionFeedbackRepo: SessionFeedbackRepo) : ViewModel() {
    private val sessionDataStateMediatorLiveData = NonNullMediatorLiveData<SessionsModel>()
    private val sessionDataError = NonNullMediatorLiveData<String>()
    private val speakersStateMediatorLiveData = NonNullMediatorLiveData<List<SpeakersModel>>()
    private val speakersError = NonNullMediatorLiveData<String>()
    private val roomStateMediatorLiveData = NonNullMediatorLiveData<RoomModel>()
    private val roomError = NonNullMediatorLiveData<String>()
    private val sessionFeedBackMediatorLiveData = NonNullMediatorLiveData<String>()
    private val sessionFeedbackError = NonNullMediatorLiveData<String>()

    fun getSessionDataResponse(): LiveData<SessionsModel> = sessionDataStateMediatorLiveData

    fun getSessionDataError(): LiveData<String> = sessionDataError

    fun getSpeakerInfoResponse(): LiveData<List<SpeakersModel>> = speakersStateMediatorLiveData

    fun getSpeakerError(): LiveData<String> = speakersError

    fun getRoomInfoResponse(): LiveData<RoomModel> = roomStateMediatorLiveData

    fun getRoomInfoError(): LiveData<String> = roomError

    fun getSessionFeedBackResponse(): LiveData<String> = sessionFeedBackMediatorLiveData

    fun getSessionFeedbackError(): LiveData<String> = sessionFeedbackError


    fun fetchSpeakerDetails(speakerId: Int) {
        viewModelScope.launch {
            when (val value = speakersRepo.getSpeakersInfo(speakerId)) {
                is Result.Success -> speakersStateMediatorLiveData.postValue(value.data)
                is Result.Error -> speakersError.postValue(value.exception)
            }
        }
    }

    fun fetchRoomDetails(roomId: Int) {
        viewModelScope.launch {
            when (val value = roomRepo.getRoomDetails(roomId)) {
                is Result.Success -> roomStateMediatorLiveData.postValue(value.data)
                is Result.Error -> roomError.postValue(value.exception)
            }
        }
    }

    fun getSessionDetails(dayNumber: String, sessionId: Int) {
        viewModelScope.launch {
            when (val value = sessionDataRepo.getSessionData(dayNumber, sessionId)) {
                is Result.Success -> sessionDataStateMediatorLiveData.postValue(value.data)
                is Result.Error -> sessionDataError.postValue(value.exception)
            }
        }
    }


    fun sendSessionFeedBack(userEventFeedback: SessionsUserFeedback) {
        viewModelScope.launch {
            when (val value = sessionFeedbackRepo.sendFeedBack(userEventFeedback)) {
                is Result.Success -> sessionFeedBackMediatorLiveData.postValue(value.data)
                is Result.Error -> sessionFeedbackError.postValue(value.exception)
            }

        }
    }

}
