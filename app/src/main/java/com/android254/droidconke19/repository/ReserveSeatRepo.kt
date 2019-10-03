package com.android254.droidconke19.repository

import com.android254.droidconke19.datastates.Result
import com.android254.droidconke19.models.ReserveSeatModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.tasks.await

interface ReserveSeatRepo {
    suspend fun reserveSeat(reserveSeatModel: ReserveSeatModel): Result<String>

    suspend fun unReserveSeat(reserveSeatModel: ReserveSeatModel): Result<String>
}

class ReserveSeatRepoImpl(val firestore: FirebaseFirestore) : ReserveSeatRepo {


    override suspend fun reserveSeat(reserveSeatModel: ReserveSeatModel): Result<String> {
        if (!isSeatReserved(reserveSeatModel)) {
            return try {
                firestore.collection("reserved_seats").add(reserveSeatModel).await()
                Result.Success("Seat successfully reserved")

            } catch (e: FirebaseFirestoreException) {
                Result.Error(e.message)
            }
        }
        return Result.Error("Seat already reserved")
    }

    private suspend fun isSeatReserved(reserveSeatModel: ReserveSeatModel): Boolean {
        return try {
            val snapshot = firestore.collection("reserved_seats")
                    .whereEqualTo("day_number", reserveSeatModel.day_number)
                    .whereEqualTo("session_id", reserveSeatModel.session_id)
                    .whereEqualTo("user_id", reserveSeatModel.user_id)
                    .get()
                    .await()
            !snapshot.isEmpty
        } catch (e: FirebaseFirestoreException) {
            false
        }
    }

    override suspend fun unReserveSeat(reserveSeatModel: ReserveSeatModel): Result<String> {
        return try {
            val snapshot = firestore.collection("reserved_seats")
                    .whereEqualTo("day_number", reserveSeatModel.day_number)
                    .whereEqualTo("session_id", reserveSeatModel.session_id)
                    .whereEqualTo("user_id", reserveSeatModel.user_id)
                    .get()
                    .await()
            if (!snapshot.isEmpty) {
                snapshot.documents.first().reference.delete().await()
            }
            Result.Success("Seat un-reserved")
        } catch (e: FirebaseFirestoreException) {
            Result.Error(e.message)
        }
    }
}