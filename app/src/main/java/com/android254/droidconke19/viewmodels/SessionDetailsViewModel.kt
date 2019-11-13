package com.android254.droidconke19.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.android254.droidconke19.datastates.FirebaseResult
import com.android254.droidconke19.models.ReserveSeatModel
import com.android254.droidconke19.models.SessionsModel
import com.android254.droidconke19.repository.ReserveSeatRepo
import com.android254.droidconke19.repository.SessionDataRepo
import com.android254.droidconke19.utils.SharedPref
import com.android254.droidconke19.utils.SingleLiveEvent
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class SessionDetailsViewModel(private val firebaseMessaging: FirebaseMessaging,
                              private val sessionDataRepo: SessionDataRepo, private val reserveSeatRepo: ReserveSeatRepo) : ViewModel() {
    val message = SingleLiveEvent<String>()
    private val reserveSeatMediatorLiveData = SingleLiveEvent<String>()

    fun getReserveSeatResponse(): LiveData<String> = reserveSeatMediatorLiveData

    suspend fun addToFavourites(sharedPreferences: SharedPreferences, userId: String, sessionsModel: SessionsModel): Boolean = withContext(Dispatchers.IO) {
        val slug = sessionsModel.notification_slug
        val dayNumber = sessionsModel.day_number
        val sessionId = sessionsModel.id
        val favourites = sharedPreferences.getStringSet(SharedPref.FAVOURITE_SESSIONS, mutableSetOf())!!
        return@withContext if (favourites.contains(slug)) {
            favourites.remove(slug)
            firebaseMessaging.unsubscribeFromTopic(slug).await()
            when (val value = sessionDataRepo.unstarrSession(dayNumber, sessionId, userId)) {
                is FirebaseResult.Success -> message.postValue(value.data)
                is FirebaseResult.Error -> message.postValue(value.exception)
            }
            sharedPreferences.edit().putStringSet(SharedPref.FAVOURITE_SESSIONS, favourites).apply()
            false
        } else {
            favourites.add(slug)
            firebaseMessaging.subscribeToTopic(slug).await()
            when (val value = sessionDataRepo.starrSession(dayNumber, sessionId, userId, slug)) {
                is FirebaseResult.Success -> message.postValue(value.data)
                is FirebaseResult.Error -> message.postValue(value.exception)
            }
            sharedPreferences.edit().putStringSet(SharedPref.FAVOURITE_SESSIONS, favourites).apply()
            true
        }
    }

    suspend fun fetchFavourites(sharedPreferences: SharedPreferences, userId: String) = withContext(Dispatchers.IO) {
        when (val value = sessionDataRepo.getStarredSessions(userId)) {
            is FirebaseResult.Success -> {
                val favourites = value.data
                if (favourites.isNotEmpty()) {
                    sharedPreferences.edit().putStringSet(SharedPref.FAVOURITE_SESSIONS, favourites.toSet()).apply()
                }
            }
            is FirebaseResult.Error -> message.postValue(value.exception)
        }
    }

    fun isFavourite(sharedPreferences: SharedPreferences, sessionsModel: SessionsModel): Boolean {
        val slug = sessionsModel.notification_slug
        val favourites = sharedPreferences.getStringSet(SharedPref.FAVOURITE_SESSIONS, mutableSetOf())!!
        return favourites.contains(slug)
    }

    suspend fun removeAllFavourites(sharedPreferences: SharedPreferences, userId: String) {
        val favourites = sharedPreferences.getStringSet(SharedPref.FAVOURITE_SESSIONS, mutableSetOf())!!
        for (favourite in favourites) {
            firebaseMessaging.unsubscribeFromTopic(favourite).await()
        }
        sessionDataRepo.clearStarredSessions(userId)
        sharedPreferences.edit().putStringSet(SharedPref.FAVOURITE_SESSIONS, mutableSetOf()).apply()
    }

    suspend fun reserveSeat(reserveSeatModel: ReserveSeatModel, sharedPreferences: SharedPreferences, sessionsModel: SessionsModel): Boolean = withContext(Dispatchers.IO) {
        val reservedSeats = sharedPreferences.getStringSet(SharedPref.RESERVED_SEATS, mutableSetOf())!!
        return@withContext if (reservedSeats.contains(sessionsModel.title)) {
            reservedSeats.remove(sessionsModel.title)
            when (val value = reserveSeatRepo.unReserveSeat(reserveSeatModel)) {
                is FirebaseResult.Success -> reserveSeatMediatorLiveData.postValue(value.data)
                is FirebaseResult.Error -> reserveSeatMediatorLiveData.postValue(value.exception)
            }
            sharedPreferences.edit().putStringSet(SharedPref.RESERVED_SEATS, reservedSeats).apply()
            true
        } else {
            reservedSeats.add(sessionsModel.title)
            when (val value = reserveSeatRepo.reserveSeat(reserveSeatModel)) {
                is FirebaseResult.Success -> reserveSeatMediatorLiveData.postValue(value.data)
                is FirebaseResult.Error -> reserveSeatMediatorLiveData.postValue(value.exception)
            }
            sharedPreferences.edit().putStringSet(SharedPref.RESERVED_SEATS, reservedSeats).apply()
            true
        }
    }

    fun isSeatReserved(sharedPreferences: SharedPreferences, sessionsModel: SessionsModel): Boolean {
        val reservedSeats = sharedPreferences.getStringSet(SharedPref.RESERVED_SEATS, mutableSetOf())!!
        return reservedSeats.contains(sessionsModel.title)
    }

}