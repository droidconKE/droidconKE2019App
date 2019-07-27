package com.android254.droidconke19.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.android254.droidconke19.datastates.Result
import kotlinx.coroutines.tasks.await

class UpdateTokenRepo(val firestore: FirebaseFirestore) {

    suspend fun updateToken(userId: String, refreshToken: String): Result<Boolean> {
        return try {
            firestore.collection("users").document(userId).update("refresh_token", refreshToken).await()
            Result.Success(true)
        } catch (e: FirebaseFirestoreException) {
            Result.Error(e.message)
        }


    }
}
