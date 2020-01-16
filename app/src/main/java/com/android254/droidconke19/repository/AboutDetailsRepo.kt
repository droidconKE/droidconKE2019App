package com.android254.droidconke19.repository

import com.android254.droidconke19.datastates.FirebaseResult
import com.android254.droidconke19.datastates.runCatching
import com.android254.droidconke19.models.AboutDetailsModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.tasks.await

interface AboutDetailsRepo {
    suspend fun getAboutDetails(aboutType: String): FirebaseResult<List<AboutDetailsModel>>
}


class AboutDetailsRepoImpl(val firestore: FirebaseFirestore) : AboutDetailsRepo {

    override suspend fun getAboutDetails(aboutType: String): FirebaseResult<List<AboutDetailsModel>> =
            runCatching {
                val snapshot = firestore.collection(aboutType)
                        .orderBy("id", Query.Direction.ASCENDING)
                        .get()
                        .await()
                snapshot.toObjects<AboutDetailsModel>()
            }
}
