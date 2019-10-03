package com.android254.droidconke19.repository

import com.android254.droidconke19.datastates.Result
import com.android254.droidconke19.models.Announcement
import com.android254.droidconke19.utils.await
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.toObjects

interface AnnouncementRepo {
    suspend fun getAnnouncements(): Result<List<Announcement>>
}

class AnnouncementRepoImpl(val firestore: FirebaseFirestore) : AnnouncementRepo {

    override suspend fun getAnnouncements(): Result<List<Announcement>> {
        return try {
            val snapshot = firestore.collection("announcements").get().await()
            return Result.Success(snapshot.toObjects())

        } catch (e: FirebaseFirestoreException) {
            Result.Error(e.message)
        }

    }
}