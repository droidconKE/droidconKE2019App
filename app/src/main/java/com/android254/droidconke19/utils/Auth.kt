package com.android254.droidconke19.utils

import com.google.firebase.auth.FirebaseAuth

fun FirebaseAuth.isSignedIn(): Boolean {
    val user = this.currentUser
    return currentUser != null
}