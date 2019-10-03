package com.android254.droidconke19.repository

import com.android254.droidconke19.datastates.Result
import com.android254.droidconke19.models.AboutDetailsModel
import com.android254.droidconke19.utils.await
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObjects

interface AboutDetailsRepo {
    suspend fun getAboutDetails(aboutType: String): Result<List<AboutDetailsModel>>
}


class AboutDetailsRepoImpl(val firestore: FirebaseFirestore) : AboutDetailsRepo {

    override suspend fun getAboutDetails(aboutType: String): Result<List<AboutDetailsModel>> {
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
