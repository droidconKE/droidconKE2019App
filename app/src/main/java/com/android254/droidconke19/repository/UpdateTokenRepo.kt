package com.android254.droidconke19.repository

import com.android254.droidconke19.datastates.Result
import com.android254.droidconke19.datastates.runCatching
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

interface UpdateTokenRepo {
    suspend fun updateToken(userId: String, refreshToken: String): Result<Boolean>
}

class UpdateTokenRepoImpl(val firestore: FirebaseFirestore) : UpdateTokenRepo {

    override suspend fun updateToken(userId: String, refreshToken: String): Result<Boolean> =
            runCatching {
                firestore.collection("users")
                        .document(userId)
                        .update("refresh_token", refreshToken)
                        .await()
                true
            }

}
