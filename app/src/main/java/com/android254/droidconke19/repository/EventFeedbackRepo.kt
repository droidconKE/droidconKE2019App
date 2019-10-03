package com.android254.droidconke19.repository

import com.android254.droidconke19.datastates.Result
import com.android254.droidconke19.models.UserEventFeedback
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.tasks.await

interface EventFeedbackRepo {
    suspend fun sendFeedBack(userEventFeedback: UserEventFeedback): Result<String>
}

class EventFeedbackRepoImpl(val firestore: FirebaseFirestore) : EventFeedbackRepo {

    override suspend fun sendFeedBack(userEventFeedback: UserEventFeedback): Result<String> {
        return try {
            firestore.collection("event_feedback_2019").add(userEventFeedback).await()
            Result.Success("Thank you for your feedback")

        } catch (e: FirebaseFirestoreException) {
            Result.Error(e.message)
        }
    }
}