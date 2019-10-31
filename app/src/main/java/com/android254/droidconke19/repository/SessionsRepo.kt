package com.android254.droidconke19.repository

import com.android254.droidconke19.database.AppDatabase
import com.android254.droidconke19.datastates.Result
import com.android254.droidconke19.datastates.runCatching
import com.android254.droidconke19.models.SessionsModel
import com.android254.droidconke19.models.SpeakersModel
import com.android254.droidconke19.utils.await
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObjects

interface SessionsRepo {
    suspend fun getSessions(sessionDay: String): Result<List<SessionsModel>>
}

class SessionsRepoImpl(db: AppDatabase, private val firestore: FirebaseFirestore) : SessionsRepo {

    override suspend fun getSessions(sessionDay: String): Result<List<SessionsModel>> =
            runCatching {
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
            }

    private suspend fun getSpeakerInfo(): List<SpeakersModel> =
            firestore.collection("speakers2019")
                    .get()
                    .await()
                    .toObjects()

}
