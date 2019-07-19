package com.android254.droidconke19.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.android254.droidconke19.models.SessionsModel
import com.android254.droidconke19.utils.NonNullMediatorLiveData

class SessionDetailsViewModel : ViewModel() {
    private val sessionDetailsMediatorLiveData = NonNullMediatorLiveData<SessionsModel>()

    fun getSessionDetails():LiveData<SessionsModel> = sessionDetailsMediatorLiveData

    fun loadSessionDetails(sessionsModel: SessionsModel){
        sessionDetailsMediatorLiveData.value = sessionsModel
    }


}