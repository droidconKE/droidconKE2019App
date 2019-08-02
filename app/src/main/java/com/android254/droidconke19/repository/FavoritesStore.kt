package com.android254.droidconke19.repository

import android.content.SharedPreferences
import com.android254.droidconke19.models.SessionsModel
import com.android254.droidconke19.utils.SharedPref

class FavoritesStore(private val sharedPreferences: SharedPreferences) {

    private val favorites: Set<String>
        get() = sharedPreferences.getStringSet(SharedPref.FAVOURITE_SESSIONS, mutableSetOf())!!

    fun isFavorite(event: SessionsModel): Boolean {
        return favorites.contains(event.title)
    }

    fun toggleFavorite(event: SessionsModel) {
        val items = favorites.toMutableSet()

        if (items.contains(event.title)) {
            items.remove(event.title)
        } else {
            items.add(event.title)
        }

        sharedPreferences.edit().putStringSet(SharedPref.FAVOURITE_SESSIONS, items).apply()
    }
}