package com.android254.droidconke19.repository

import com.android254.droidconke19.datastates.Result
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.tasks.await

interface UpdateTokenRepo {
    suspend fun updateToken(userId: String, refreshToken: String): Result<Boolean>
}

class UpdateTokenRepoImpl(val firestore: FirebaseFirestore) : UpdateTokenRepo {

    override suspend fun updateToken(userId: String, refreshToken: String): Result<Boolean> {
        return try {
            firestore.collection("users").document(userId).update("refresh_token", refreshToken).await()
            Result.Success(true)
        } catch (e: FirebaseFirestoreException) {
            Result.Error(e.message)
        }


    }
}
