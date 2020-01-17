package com.android254.droidconke19.repository

import com.android254.droidconke19.datastates.FirebaseResult
import com.android254.droidconke19.utils.runCatching
import com.android254.droidconke19.models.TravelInfoModel
import com.google.firebase.remoteconfig.FirebaseRemoteConfig

interface TravelDetailsRepo {
    suspend fun fetchTravelDetails(): FirebaseResult<TravelInfoModel>
}

class TravelDetailsRepoImpl(private val firebaseRemoteConfig: FirebaseRemoteConfig) : TravelDetailsRepo {
    override suspend fun fetchTravelDetails(): FirebaseResult<TravelInfoModel> = runCatching {
        firebaseRemoteConfig.fetchAndActivate()
        TravelInfoModel(firebaseRemoteConfig.getString("driving_directions"), firebaseRemoteConfig.getString("public_transportation"), firebaseRemoteConfig.getString("car_pooling_parking_info"),
                firebaseRemoteConfig.getString("ride_sharing"))

    }


}