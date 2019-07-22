package com.android254.droidconke19.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObjects
import com.android254.droidconke19.database.AppDatabase
import com.android254.droidconke19.database.dao.SessionsDao
import com.android254.droidconke19.datastates.Result
import com.android254.droidconke19.models.SessionsModel
import com.android254.droidconke19.utils.await

class DayOneRepo(db: AppDatabase, private val firestore: FirebaseFirestore) {
    private val sessionsDao: SessionsDao = db.sessionsDao()

    suspend fun getDayOneSessions(): Result<List<SessionsModel>> {
        return try {
            val snapshots = firestore.collection("day_one")
                    .orderBy("id", Query.Direction.ASCENDING)
                    .get().await()
            saveSession(snapshots.toObjects())
            Result.Success(snapshots.toObjects())
        } catch (e: FirebaseFirestoreException) {
            Result.Error(e.message)
        }
    }

    suspend fun saveSession(sessionsModelList: List<SessionsModel>) {
//        sessionsDao.saveSession(sessionsModelList)
    }
}
