package com.android254.droidconke19.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.toObjects
import com.android254.droidconke19.datastates.Result
import com.android254.droidconke19.models.SpeakersModel
import com.android254.droidconke19.utils.await

class SpeakersRepo(private val firestore: FirebaseFirestore) {

    suspend fun getSpeakersInfo(speakerId: Int): Result<List<SpeakersModel>> {
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
