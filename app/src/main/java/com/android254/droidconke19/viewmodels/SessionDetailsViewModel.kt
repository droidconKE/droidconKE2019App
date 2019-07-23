package com.android254.droidconke19.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.android254.droidconke19.datastates.Result
import com.android254.droidconke19.models.SessionsModel
import com.android254.droidconke19.repository.SessionDataRepo
import com.android254.droidconke19.utils.NonNullMediatorLiveData
import com.android254.droidconke19.utils.SharedPref
import com.android254.droidconke19.utils.SingleLiveEvent
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class SessionDetailsViewModel(private val firebaseMessaging: FirebaseMessaging,
                              private val sessionDataRepo: SessionDataRepo) : ViewModel() {
    private val sessionDetailsMediatorLiveData = NonNullMediatorLiveData<SessionsModel>()
    val message = SingleLiveEvent<String>()

    fun getSessionDetails(): LiveData<SessionsModel> = sessionDetailsMediatorLiveData

    fun loadSessionDetails(sessionsModel: SessionsModel) {
        sessionDetailsMediatorLiveData.value = sessionsModel
    }


    suspend fun addToFavourites(sharedPreferences: SharedPreferences, userId: String): Boolean = withContext(Dispatchers.IO) {
        val slug = sessionDetailsMediatorLiveData.value!!.notification_slug
        val dayNumber = sessionDetailsMediatorLiveData.value!!.day_number
        val sessionId = sessionDetailsMediatorLiveData.value!!.id
        val favourites = sharedPreferences.getStringSet(SharedPref.FAVOURITE_SESSIONS, mutableSetOf())!!
        return@withContext if (favourites.contains(slug)) {
            favourites.remove(slug)
            firebaseMessaging.unsubscribeFromTopic(slug).await()
            when (val value = sessionDataRepo.unstarrSession(dayNumber, sessionId, userId)) {
                is Result.Success -> message.postValue(value.data)
                is Result.Error -> message.postValue(value.exception)
            }
            sharedPreferences.edit().putStringSet(SharedPref.FAVOURITE_SESSIONS, favourites).apply()
            false
        } else {
            favourites.add(slug)
            firebaseMessaging.subscribeToTopic(slug).await()
            when (val value = sessionDataRepo.starrSession(dayNumber, sessionId, userId)) {
                is Result.Success -> message.postValue(value.data)
                is Result.Error -> message.postValue(value.exception)
            }
            sharedPreferences.edit().putStringSet(SharedPref.FAVOURITE_SESSIONS, favourites).apply()
            true
        }
    }

    fun isFavourite(sharedPreferences: SharedPreferences): Boolean {
        val slug = sessionDetailsMediatorLiveData.value!!.notification_slug
        val favourites = sharedPreferences.getStringSet(SharedPref.FAVOURITE_SESSIONS, mutableSetOf())!!
        return favourites.contains(slug)
    }

    suspend fun removeAllFavourites(sharedPreferences: SharedPreferences) {
        val favourites = sharedPreferences.getStringSet(SharedPref.FAVOURITE_SESSIONS, mutableSetOf())!!
        for (favourite in favourites) {
            firebaseMessaging.unsubscribeFromTopic(favourite).await()
        }
        sharedPreferences.edit().putStringSet(SharedPref.FAVOURITE_SESSIONS, mutableSetOf()).apply()
    }


}