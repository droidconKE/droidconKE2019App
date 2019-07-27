package com.android254.droidconke19.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android254.droidconke19.datastates.Result
import com.android254.droidconke19.repository.UpdateTokenRepo
import com.android254.droidconke19.utils.NonNullMediatorLiveData
import kotlinx.coroutines.launch

class HomeViewModel(private val updateTokenRepo: UpdateTokenRepo) : ViewModel() {
    private val updateTokenStateMediatorLiveData = NonNullMediatorLiveData<Boolean>()
    private val updateTokenError = NonNullMediatorLiveData<String>()

    fun getUpdateTokenResponse(): LiveData<Boolean> = updateTokenStateMediatorLiveData

    fun getUpdateTokenError(): LiveData<String> = updateTokenError


    fun updateToken(userId: String, refreshToken: String) {
        viewModelScope.launch {
            when (val value = updateTokenRepo.updateToken(userId, refreshToken)) {
                is Result.Success -> updateTokenStateMediatorLiveData.postValue(value.data)
                is Result.Error -> updateTokenError.postValue(value.exception)
            }
        }

    }
}
