package com.android254.droidconke19.firebase

import android.content.SharedPreferences
import com.android254.droidconke19.utils.SharedPref.FIREBASE_TOKEN
import com.google.firebase.messaging.FirebaseMessagingService
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf


class InstanceIdService : FirebaseMessagingService() {

    private val sharedPreferences: SharedPreferences by inject { parametersOf(this) }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        saveToken(token)
    }

    private fun saveToken(token: String?) {
        sharedPreferences.edit().putString(FIREBASE_TOKEN, token).apply()

    }
}
