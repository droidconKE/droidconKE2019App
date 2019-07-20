package com.android254.droidconke19.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.android254.droidconke19.models.SessionsModel
import com.android254.droidconke19.utils.NonNullMediatorLiveData
import com.android254.droidconke19.utils.SharedPref
import com.google.firebase.messaging.FirebaseMessaging

class SessionDetailsViewModel(private val firebaseMessaging: FirebaseMessaging) : ViewModel() {
    private val sessionDetailsMediatorLiveData = NonNullMediatorLiveData<SessionsModel>()

    fun getSessionDetails(): LiveData<SessionsModel> = sessionDetailsMediatorLiveData

    fun loadSessionDetails(sessionsModel: SessionsModel) {
        sessionDetailsMediatorLiveData.value = sessionsModel
    }


    fun addToFavourites(sharedPreferences: SharedPreferences): Boolean {
        val slug = sessionDetailsMediatorLiveData.value!!.notification_slug
        val favourites = sharedPreferences.getStringSet(SharedPref.FAVOURITE_SESSIONS, mutableSetOf())!!
        return if (favourites.contains(slug)) {
            favourites.remove(slug)
            firebaseMessaging.unsubscribeFromTopic(slug)
            sharedPreferences.edit().putStringSet(SharedPref.FAVOURITE_SESSIONS, favourites).apply()
            false
        } else {
            favourites.add(slug)
            firebaseMessaging.subscribeToTopic(slug)
            sharedPreferences.edit().putStringSet(SharedPref.FAVOURITE_SESSIONS, favourites).apply()
            true
        }
    }

    fun isFavourite(sharedPreferences: SharedPreferences): Boolean {
        val slug = sessionDetailsMediatorLiveData.value!!.notification_slug
        val favourites = sharedPreferences.getStringSet(SharedPref.FAVOURITE_SESSIONS, mutableSetOf())!!
        return favourites.contains(slug)
    }


}