package com.android254.droidconke19.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "starredSessions")
data class StarredSessionModel (
        @PrimaryKey
        val documentId: String,
        val day: String,
        val session_id: String,
        val user_id: String,
        val isStarred: Boolean
)
