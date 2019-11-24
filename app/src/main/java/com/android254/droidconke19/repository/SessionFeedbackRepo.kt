package com.android254.droidconke19.repository

import com.android254.droidconke19.datastates.FirebaseResult
import com.android254.droidconke19.datastates.runCatching
import com.android254.droidconke19.models.SessionsUserFeedback
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

interface SessionFeedbackRepo {
    suspend fun sendFeedBack(userSessionFeedback: SessionsUserFeedback): FirebaseResult<String>
}

class SessionFeedbackRepoImpl(private val firestore: FirebaseFirestore) : SessionFeedbackRepo {

    override suspend fun sendFeedBack(userSessionFeedback: SessionsUserFeedback): FirebaseResult<String> =
            runCatching {
                firestore.collection("session_feedback_2019")
                        .add(userSessionFeedback)
                        .await()
                "Thank you for your feedback"
            }
}
