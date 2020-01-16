package com.android254.droidconke19.utils

import com.android254.droidconke19.models.WifiDetailsModel
import com.android254.droidconke19.models.isPropertiesNotEmpty
import com.google.firebase.remoteconfig.FirebaseRemoteConfig

object WifiDetailsModelFactory {
    fun create(firebaseRemoteConfig: FirebaseRemoteConfig): WifiDetailsModel {
        val wifiDetailsModel = WifiDetailsModel(
                firebaseRemoteConfig.getString("wifi_ssid"),
                firebaseRemoteConfig.getString("wifi_password")
        )

        if (!wifiDetailsModel.isPropertiesNotEmpty)
            throw Exception("No Firebase RemoteConfig default values provided for wifi details")

        return wifiDetailsModel
    }
}