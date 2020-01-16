package com.android254.droidconke19.repository

import com.android254.droidconke19.datastates.FirebaseResult
import com.android254.droidconke19.datastates.runCatching
import com.android254.droidconke19.models.AgendaModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.tasks.await

interface AgendaRepo {
    suspend fun agendaData(): FirebaseResult<List<AgendaModel>>
}

class AgendaRepoImpl(val firestore: FirebaseFirestore) : AgendaRepo {

    override suspend fun agendaData(): FirebaseResult<List<AgendaModel>> =
            runCatching {
                val snapshot = firestore
                        .collection("agenda_2019")
                        .orderBy("id", Query.Direction.ASCENDING)
                        .get()
                        .await()
                snapshot.toObjects<AgendaModel>()
            }

}
