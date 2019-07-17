package com.android254.droidconke19.firebase

import android.content.Context
import com.google.firebase.messaging.FirebaseMessagingService
import com.android254.droidconke19.utils.SharedPref.FIREBASE_TOKEN
import com.android254.droidconke19.utils.SharedPref.PREF_NAME


class InstanceIdService : FirebaseMessagingService() {
    override fun onNewToken(token: String?) {
        super.onNewToken(token)
        saveToken(token)
    }

    private fun saveToken(token: String?) {
        val sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(FIREBASE_TOKEN, token).apply()

    }
}
