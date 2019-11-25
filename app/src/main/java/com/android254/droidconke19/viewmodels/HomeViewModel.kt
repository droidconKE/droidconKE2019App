package com.android254.droidconke19.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android254.droidconke19.datastates.FirebaseResult
import com.android254.droidconke19.repository.UpdateTokenRepo
import kotlinx.coroutines.launch

class HomeViewModel(private val updateTokenRepo: UpdateTokenRepo) : ViewModel() {
    private val updateTokenStateMediatorLiveData = MediatorLiveData<Boolean>()
    private val updateTokenError = MediatorLiveData<String>()

    fun getUpdateTokenResponse(): LiveData<Boolean> = updateTokenStateMediatorLiveData

    fun getUpdateTokenError(): LiveData<String> = updateTokenError


    fun updateToken(userId: String, refreshToken: String) {
        viewModelScope.launch {
            when (val value = updateTokenRepo.updateToken(userId, refreshToken)) {
                is FirebaseResult.Success -> updateTokenStateMediatorLiveData.postValue(value.data)
                is FirebaseResult.Error -> updateTokenError.postValue(value.exception)
            }
        }

    }
}
