package com.android254.droidconke19.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android254.droidconke19.datastates.FirebaseResult
import com.android254.droidconke19.models.AgendaModel
import com.android254.droidconke19.repository.AgendaRepo
import kotlinx.coroutines.launch

class AgendaViewModel(private val agendaRepo: AgendaRepo) : ViewModel() {
    private val agendaStateMediatorLiveData = MediatorLiveData<List<AgendaModel>>()
    private val firebaseError = MediatorLiveData<String>()

    fun getAgendasResponse(): LiveData<List<AgendaModel>> = agendaStateMediatorLiveData

    fun getFirebaseError(): LiveData<String> = firebaseError

    fun fetchAgendas() {
        viewModelScope.launch {
            when (val value = agendaRepo.agendaData()) {
                is FirebaseResult.Success -> agendaStateMediatorLiveData.postValue(value.data)
                is FirebaseResult.Error -> firebaseError.postValue(value.exception)
            }
        }
    }
}
