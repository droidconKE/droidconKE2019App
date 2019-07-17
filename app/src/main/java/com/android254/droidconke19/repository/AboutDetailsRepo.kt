package com.android254.droidconke19.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObjects
import com.android254.droidconke19.datastates.Result
import com.android254.droidconke19.models.AboutDetailsModel
import com.android254.droidconke19.utils.await

class AboutDetailsRepo(val firestore: FirebaseFirestore) {

    suspend fun getAboutDetails(aboutType: String): Result<List<AboutDetailsModel>> {
        return try {
            val snapshot = firestore.collection(aboutType)
                    .orderBy("id", Query.Direction.ASCENDING)
                    .get()
                    .await()
            val aboutDetailsModelList = snapshot.toObjects<AboutDetailsModel>()
            Result.Success(aboutDetailsModelList)
        } catch (e: Exception) {
            Result.Error( e.message)
        }
    }
}
