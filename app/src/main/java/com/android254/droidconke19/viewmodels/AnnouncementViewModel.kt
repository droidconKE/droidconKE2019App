package com.android254.droidconke19.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android254.droidconke19.datastates.Result
import com.android254.droidconke19.models.Announcement
import com.android254.droidconke19.repository.AnnouncementRepo
import com.android254.droidconke19.utils.NonNullMediatorLiveData
import kotlinx.coroutines.launch

class AnnouncementViewModel(private val announcementRepo: AnnouncementRepo) : ViewModel() {
    private val announcementMediatorLiveData = NonNullMediatorLiveData<List<Announcement>>()
    private val announcementError = NonNullMediatorLiveData<String>()


    fun getAnnouncementsResponse(): LiveData<List<Announcement>> = announcementMediatorLiveData

    fun getAnnouncementError(): LiveData<String> = announcementError

    fun getAnnouncements() {
        viewModelScope.launch {
            when (val value = announcementRepo.getAnnouncements()) {
                is Result.Success -> announcementMediatorLiveData.postValue(value.data)
                is Result.Error -> announcementError.postValue(value.exception)
            }
        }
    }
}