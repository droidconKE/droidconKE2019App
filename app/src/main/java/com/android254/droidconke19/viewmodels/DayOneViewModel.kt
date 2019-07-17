package com.android254.droidconke19.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android254.droidconke19.datastates.Result
import com.android254.droidconke19.models.SessionsModel
import com.android254.droidconke19.repository.DayOneRepo
import com.android254.droidconke19.utils.NonNullMediatorLiveData
import kotlinx.coroutines.launch

class DayOneViewModel(private val dayOneRepo: DayOneRepo) : ViewModel() {
    private val sessionsStateMediatorLiveData = NonNullMediatorLiveData<List<SessionsModel>>()
    private val sessionError = NonNullMediatorLiveData<String>()


    fun getSessionsResponse(): LiveData<List<SessionsModel>> = sessionsStateMediatorLiveData

    fun getSessionsError(): LiveData<String> = sessionError

    fun getDayOneSessions() {
        viewModelScope.launch {
            when (val value = dayOneRepo.getDayOneSessions()) {
                is Result.Success -> sessionsStateMediatorLiveData.postValue(value.data)
                is Result.Error -> sessionError.postValue(value.exception)
            }
        }
    }
}
