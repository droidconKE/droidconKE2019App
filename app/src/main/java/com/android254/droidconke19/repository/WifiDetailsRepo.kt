package com.android254.droidconke19.repository

import com.android254.droidconke19.datastates.FirebaseResult
import com.android254.droidconke19.datastates.runCatching
import com.android254.droidconke19.models.WifiDetailsModel
import com.android254.droidconke19.models.utils.WifiDetailsModelFactory
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.coroutines.tasks.await

interface WifiDetailsRepo {
    suspend fun fetchWifiDetails(): FirebaseResult<WifiDetailsModel>
}

class WifiDetailsRepoImpl(private val firebaseRemoteConfig: FirebaseRemoteConfig) : WifiDetailsRepo {

    override suspend fun fetchWifiDetails(): FirebaseResult<WifiDetailsModel> =
            runCatching {
                // Fetch Firebase RemoteConfig values
                firebaseRemoteConfig.fetchAndActivate().await()

                // Create an instance of WifiDetailsModels from active Firebase RemoteConfig values
                WifiDetailsModelFactory.create(firebaseRemoteConfig)
            }
}
