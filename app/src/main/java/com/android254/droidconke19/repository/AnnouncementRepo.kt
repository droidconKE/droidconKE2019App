package com.android254.droidconke19.repository

import com.android254.droidconke19.datastates.FirebaseResult
import com.android254.droidconke19.models.Announcement
import com.android254.droidconke19.utils.await
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.toObjects

interface AnnouncementRepo {
    suspend fun getAnnouncements(): FirebaseResult<List<Announcement>>
}

class AnnouncementRepoImpl(val firestore: FirebaseFirestore) : AnnouncementRepo {

    override suspend fun getAnnouncements(): FirebaseResult<List<Announcement>> {
        return try {
            val snapshot = firestore.collection("announcements").get().await()
            return FirebaseResult.Success(snapshot.toObjects())

        } catch (e: FirebaseFirestoreException) {
            FirebaseResult.Error(e.message)
        }

    }
}