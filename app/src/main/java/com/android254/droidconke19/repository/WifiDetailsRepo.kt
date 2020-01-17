package com.android254.droidconke19.repository

import com.android254.droidconke19.datastates.FirebaseResult
import com.android254.droidconke19.models.WifiDetailsModel
import com.android254.droidconke19.utils.runCatching
import com.google.firebase.remoteconfig.FirebaseRemoteConfig

interface WifiDetailsRepo {
    suspend fun fetchWifiDetails(): FirebaseResult<WifiDetailsModel>
}

class WifiDetailsRepoImpl(private val firebaseRemoteConfig: FirebaseRemoteConfig) : WifiDetailsRepo {

    override suspend fun fetchWifiDetails(): FirebaseResult<WifiDetailsModel> =
            runCatching {
                firebaseRemoteConfig.fetchAndActivate()
                WifiDetailsModel(firebaseRemoteConfig.getString("wifi_ssid"), firebaseRemoteConfig.getString("wifi_password"))
            }
}
