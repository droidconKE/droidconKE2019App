package com.android254.droidconke19.repository

import com.android254.droidconke19.datastates.Result
import com.android254.droidconke19.datastates.runCatching
import com.android254.droidconke19.models.UserEventFeedback
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

interface EventFeedbackRepo {
    suspend fun sendFeedBack(userEventFeedback: UserEventFeedback): Result<String>
}

class EventFeedbackRepoImpl(val firestore: FirebaseFirestore) : EventFeedbackRepo {

    override suspend fun sendFeedBack(userEventFeedback: UserEventFeedback): Result<String> =
            runCatching {
                firestore.collection("event_feedback_2019").add(userEventFeedback).await()
                "Thank you for your feedback"
            }

}
