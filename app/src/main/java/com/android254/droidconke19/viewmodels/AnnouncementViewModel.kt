package com.android254.droidconke19.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android254.droidconke19.datastates.FirebaseResult
import com.android254.droidconke19.models.Announcement
import com.android254.droidconke19.repository.AnnouncementRepo
import kotlinx.coroutines.launch

class AnnouncementViewModel(private val announcementRepo: AnnouncementRepo) : ViewModel() {
    private val announcementMediatorLiveData = MediatorLiveData<List<Announcement>>()
    private val firebaseError = MediatorLiveData<String>()


    fun getAnnouncementsResponse(): LiveData<List<Announcement>> = announcementMediatorLiveData

    fun getFirebaseError(): LiveData<String> = firebaseError

    fun getAnnouncements() {
        viewModelScope.launch {
            when (val value = announcementRepo.getAnnouncements()) {
                is FirebaseResult.Success -> announcementMediatorLiveData.postValue(value.data)
                is FirebaseResult.Error -> firebaseError.postValue(value.exception)
            }
        }
    }
}