package com.android254.droidconke19.repository

import com.android254.droidconke19.datastates.FirebaseResult
import com.android254.droidconke19.models.EventTypeModel
import com.android254.droidconke19.utils.await
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObjects

interface EventTypeRepo {
    suspend fun getSessionData(): FirebaseResult<List<EventTypeModel>>
}

class EventTypeRepoImpl(val firestore: FirebaseFirestore) : EventTypeRepo {

    override suspend fun getSessionData(): FirebaseResult<List<EventTypeModel>> {
        return try {
            val snapshots = firestore.collection("event_types")
                    .orderBy("id", Query.Direction.ASCENDING)
                    .get()
                    .await()
            FirebaseResult.Success(snapshots.toObjects())
        } catch (e: FirebaseFirestoreException) {
            FirebaseResult.Error(e.message)
        }
    }
}
