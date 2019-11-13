package com.android254.droidconke19.repository

import com.android254.droidconke19.datastates.Result
import com.android254.droidconke19.datastates.runCatching
import com.android254.droidconke19.models.SpeakersModel
import com.android254.droidconke19.utils.await
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects

interface SpeakersRepo {
    suspend fun getSpeakersInfo(speakerId: Int): Result<List<SpeakersModel>>
}

class SpeakersRepoImpl(private val firestore: FirebaseFirestore) : SpeakersRepo {

    override suspend fun getSpeakersInfo(speakerId: Int): Result<List<SpeakersModel>> =
            runCatching {
                val snapshot = firestore.collection("speakers2019")
                        .whereEqualTo("id", speakerId)
                        .get()
                        .await()
                snapshot.toObjects<SpeakersModel>()
            }

}
