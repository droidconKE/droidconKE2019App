package com.android254.droidconke19.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.android254.droidconke19.database.AppDatabase
import com.android254.droidconke19.database.dao.SessionsDao
import com.google.firebase.firestore.FirebaseFirestoreException
import com.android254.droidconke19.datastates.Result
import com.android254.droidconke19.models.SessionsModel
import com.android254.droidconke19.utils.await

class SessionDataRepo(db: AppDatabase, private val firestore: FirebaseFirestore) {
    private val sessionsDao: SessionsDao = db.sessionsDao()

    suspend fun getSessionData(dayNumber: String, sessionId: Int): Result<SessionsModel> {
        return try {
            val snapshot = firestore.collection(dayNumber)
                    .whereEqualTo("id", sessionId)
                    .get()
                    .await()
            val doc = snapshot.documents.first()
            val sessionsModel = doc.toObject(SessionsModel::class.java)
            val newSessionsModel = sessionsModel?.copy(documentId = doc.id)
            Result.Success(newSessionsModel!!)
        } catch (e: FirebaseFirestoreException) {
            Result.Error(e.message)
        }

    }
}
