package com.android254.droidconke19.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android254.droidconke19.datastates.FirebaseResult
import com.android254.droidconke19.models.SessionsModel
import com.android254.droidconke19.repository.SessionsRepo
import com.android254.droidconke19.utils.NonNullMediatorLiveData
import kotlinx.coroutines.launch

class SessionsViewModel(private val sessionsRepo: SessionsRepo) : ViewModel() {
    private val sessionsStateMediatorLiveData = NonNullMediatorLiveData<List<SessionsModel>>()
    private val sessionError = NonNullMediatorLiveData<String>()


    fun getSessionsResponse(): LiveData<List<SessionsModel>> = sessionsStateMediatorLiveData

    fun getSessionsError(): LiveData<String> = sessionError

    fun getSessions(sessionDay : String) {
        viewModelScope.launch {
            when (val value = sessionsRepo.getSessions(sessionDay)) {
                is FirebaseResult.Success -> sessionsStateMediatorLiveData.postValue(value.data)
                is FirebaseResult.Error -> sessionError.postValue(value.exception)
            }
        }
    }
}
