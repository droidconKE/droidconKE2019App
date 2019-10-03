package com.android254.droidconke19.repository

import com.android254.droidconke19.datastates.Result
import com.android254.droidconke19.models.SpeakersModel
import com.android254.droidconke19.utils.await
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.toObjects

interface SpeakersRepo {
    suspend fun getSpeakersInfo(speakerId: Int): Result<List<SpeakersModel>>
}

class SpeakersRepoImpl(private val firestore: FirebaseFirestore) : SpeakersRepo {

    override suspend fun getSpeakersInfo(speakerId: Int): Result<List<SpeakersModel>> {
        return try {
            val snapshot = firestore.collection("speakers2019")
                    .whereEqualTo("id", speakerId)
                    .get()
                    .await()
            Result.Success(snapshot.toObjects<SpeakersModel>())
        } catch (e: FirebaseFirestoreException) {
            Result.Error(e.message)
        }
    }
}
