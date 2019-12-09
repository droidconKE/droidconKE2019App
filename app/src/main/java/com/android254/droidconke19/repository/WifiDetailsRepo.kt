package com.android254.droidconke19.repository

import com.android254.droidconke19.datastates.FirebaseResult
import com.android254.droidconke19.models.WifiDetailsModel
import com.android254.droidconke19.models.isPropertiesNotEmpty
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

interface WifiDetailsRepo {
    suspend fun fetchWifiDetails(): FirebaseResult<WifiDetailsModel>
}

class WifiDetailsRepoImpl(private val firebaseRemoteConfig: FirebaseRemoteConfig): WifiDetailsRepo {

    override suspend fun fetchWifiDetails(): FirebaseResult<WifiDetailsModel> =
            suspendCancellableCoroutine { continuation ->
                firebaseRemoteConfig.fetchAndActivate().addOnCompleteListener {

                    val wifiDetailsModel = WifiDetailsModel(
                            firebaseRemoteConfig.getString("wifi_ssid"),
                            firebaseRemoteConfig.getString("wifi_password")
                    )

                    if (wifiDetailsModel.isPropertiesNotEmpty) {
                        continuation.resume(FirebaseResult.Success(wifiDetailsModel))
                    } else {
//                        continuation.resumeWithException(Exception(
//                                "No FRC default values provided for wifi details"))
                        continuation.resume(FirebaseResult.Error(
                                "No FRC default values provided for wifi details"))
                    }
                }.addOnCanceledListener { continuation.cancel() }
            }

}