package com.android254.droidconke19.repository

import android.content.SharedPreferences
import com.android254.droidconke19.models.SessionsModel
import com.android254.droidconke19.utils.SharedPref

class FavoritesStore(private val sharedPreferences: SharedPreferences) {

    private val favorites: MutableSet<String> = sharedPreferences.getStringSet(SharedPref.FAVOURITE_SESSIONS, mutableSetOf())!!

    fun isFavorite(sessionsModel: SessionsModel): Boolean {
        return favorites.contains(sessionsModel.notification_slug)
    }
}