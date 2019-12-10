package com.android254.droidconke19.firebase

import com.android254.droidconke19.BuildConfig
import com.android254.droidconke19.R
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

object FirebaseRemoteConfigFactory {

    fun create(): FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance().also {
        val fetchInterval = if (BuildConfig.DEBUG) 0L else 3600L

        val configSettings = FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(fetchInterval)
                .build()

        it.setConfigSettingsAsync(configSettings)
        it.setDefaultsAsync(R.xml.remote_config_defaults)

    }
}