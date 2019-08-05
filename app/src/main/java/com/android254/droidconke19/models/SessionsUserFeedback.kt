package com.android254.droidconke19.models

data class SessionsUserFeedback(
        val user_id: String,
        val session_id: Int,
        val day_number: String,
        val session_title: String,
        val session_feedback: String
)