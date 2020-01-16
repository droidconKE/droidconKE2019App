package com.android254.droidconke19.repository

import com.android254.droidconke19.datastates.FirebaseResult
import com.android254.droidconke19.datastates.runCatching
import com.android254.droidconke19.models.SpeakersModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.tasks.await

interface SpeakersRepo {
    suspend fun getSpeakersInfo(speakerId: Int): FirebaseResult<List<SpeakersModel>>
}

class SpeakersRepoImpl(private val firestore: FirebaseFirestore) : SpeakersRepo {

    override suspend fun getSpeakersInfo(speakerId: Int): FirebaseResult<List<SpeakersModel>> =
            runCatching {
                val snapshot = firestore.collection("speakers2019")
                        .whereEqualTo("id", speakerId)
                        .get()
                        .await()
                snapshot.toObjects<SpeakersModel>()
            }

}
