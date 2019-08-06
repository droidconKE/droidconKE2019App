package com.android254.droidconke19.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObjects
import com.android254.droidconke19.database.AppDatabase
import com.android254.droidconke19.datastates.Result
import com.android254.droidconke19.models.SessionsModel
import com.android254.droidconke19.models.SpeakersModel
import com.android254.droidconke19.utils.await

class SessionsRepo(db: AppDatabase, private val firestore: FirebaseFirestore) {

    suspend fun getSessions(sessionDay: String): Result<List<SessionsModel>> {
        return try {
            val snapshots = firestore.collection(sessionDay)
                    .orderBy("id", Query.Direction.ASCENDING)
                    .get().await()
            val speakerById: Map<Int, SpeakersModel> = getSpeakerInfo().associateBy { it.id }
            val sessionList: List<SessionsModel> = snapshots.toObjects()

            sessionList.onEach { sessionsModel ->
                sessionsModel.speaker_id.mapNotNull {
                    speakerById[it]?.let { speakerModel ->
                        sessionsModel.speakerList.add(speakerModel)
                    }
                }
            }
            Result.Success(sessionList)
        } catch (e: FirebaseFirestoreException) {
            Result.Error(e.message)
        }
    }

    private suspend fun getSpeakerInfo(): List<SpeakersModel> {
        val snapshot = firestore.collection("speakers2019")
                .get()
                .await()
        return snapshot.toObjects()
    }

}
