package com.android254.droidconke19.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android254.droidconke19.datastates.FirebaseResult
import com.android254.droidconke19.models.RoomModel
import com.android254.droidconke19.models.SessionsModel
import com.android254.droidconke19.models.SessionsUserFeedback
import com.android254.droidconke19.models.SpeakersModel
import com.android254.droidconke19.repository.RoomRepo
import com.android254.droidconke19.repository.SessionDataRepo
import com.android254.droidconke19.repository.SessionFeedbackRepo
import com.android254.droidconke19.repository.SpeakersRepo
import kotlinx.coroutines.launch


class SessionDataViewModel(private val sessionDataRepo: SessionDataRepo, private val speakersRepo: SpeakersRepo, private val roomRepo: RoomRepo, private val sessionFeedbackRepo: SessionFeedbackRepo) : ViewModel() {
    private val sessionDataStateMediatorLiveData = MediatorLiveData<SessionsModel>()
    private val sessionDataError = MediatorLiveData<String>()
    private val speakersStateMediatorLiveData = MediatorLiveData<List<SpeakersModel>>()
    private val speakersError = MediatorLiveData<String>()
    private val roomStateMediatorLiveData = MediatorLiveData<RoomModel>()
    private val roomError = MediatorLiveData<String>()
    private val sessionFeedBackMediatorLiveData = MediatorLiveData<String>()
    private val sessionFeedbackError = MediatorLiveData<String>()

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
                is FirebaseResult.Success -> speakersStateMediatorLiveData.postValue(value.data)
                is FirebaseResult.Error -> speakersError.postValue(value.exception)
            }
        }
    }

    fun fetchRoomDetails(roomId: Int) {
        viewModelScope.launch {
            when (val value = roomRepo.getRoomDetails(roomId)) {
                is FirebaseResult.Success -> roomStateMediatorLiveData.postValue(value.data)
                is FirebaseResult.Error -> roomError.postValue(value.exception)
            }
        }
    }

    fun getSessionDetails(dayNumber: String, sessionId: Int) {
        viewModelScope.launch {
            when (val value = sessionDataRepo.getSessionData(dayNumber, sessionId)) {
                is FirebaseResult.Success -> sessionDataStateMediatorLiveData.postValue(value.data)
                is FirebaseResult.Error -> sessionDataError.postValue(value.exception)
            }
        }
    }


    fun sendSessionFeedBack(userEventFeedback: SessionsUserFeedback) {
        viewModelScope.launch {
            when (val value = sessionFeedbackRepo.sendFeedBack(userEventFeedback)) {
                is FirebaseResult.Success -> sessionFeedBackMediatorLiveData.postValue(value.data)
                is FirebaseResult.Error -> sessionFeedbackError.postValue(value.exception)
            }

        }
    }

}
