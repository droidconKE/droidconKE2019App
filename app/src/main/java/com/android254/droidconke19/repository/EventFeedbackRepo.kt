package com.android254.droidconke19.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.android254.droidconke19.datastates.Result
import com.android254.droidconke19.models.UserEventFeedback
import kotlinx.coroutines.tasks.await


class EventFeedbackRepo(val firestore: FirebaseFirestore) {

    suspend fun sendFeedBack(userEventFeedback: UserEventFeedback): Result<String> {
        return try {
            firestore.collection("").add(userEventFeedback).await()
            Result.Success("Thank you for your feedback")

        } catch (e: FirebaseFirestoreException) {
            Result.Error(e.message)
        }
    }
}