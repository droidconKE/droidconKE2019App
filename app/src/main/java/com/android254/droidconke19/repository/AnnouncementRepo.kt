package com.android254.droidconke19.repository

import com.android254.droidconke19.datastates.FirebaseResult
import com.android254.droidconke19.datastates.runCatching
import com.android254.droidconke19.models.Announcement
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.tasks.await

interface AnnouncementRepo {
    suspend fun getAnnouncements(): FirebaseResult<List<Announcement>>
}

class AnnouncementRepoImpl(val firestore: FirebaseFirestore) : AnnouncementRepo {

    override suspend fun getAnnouncements(): FirebaseResult<List<Announcement>> =
            runCatching {
                val snapshot = firestore.collection("announcements").get().await()
                snapshot.toObjects<Announcement>()
            }

}
