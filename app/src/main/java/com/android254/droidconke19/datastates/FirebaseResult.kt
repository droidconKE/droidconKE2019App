package com.android254.droidconke19.datastates

sealed class FirebaseResult<out T : Any>{
data class Success<out T : Any>(val data: T) : FirebaseResult<T>()
data class Error(val exception: String?) : FirebaseResult<Nothing>()
}