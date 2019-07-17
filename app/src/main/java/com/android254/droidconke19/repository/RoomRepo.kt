package com.android254.droidconke19.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.android254.droidconke19.datastates.Result
import com.android254.droidconke19.models.RoomModel
import com.android254.droidconke19.utils.await

class RoomRepo(private val firestore: FirebaseFirestore) {

    suspend fun getRoomDetails(roomId: Int): Result<RoomModel> {
        return try {
            val snapshot = firestore.collection("rooms")
                    .whereEqualTo("id", roomId)
                    .get()
                    .await()
            val doc = snapshot.documents.first()
            val roomModel = doc.toObject(RoomModel::class.java)
            Result.Success(roomModel!!)

        } catch (e: FirebaseFirestoreException) {
            Result.Error(e.message)
        }

    }
}
