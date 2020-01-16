package com.android254.droidconke19.utils

import com.android254.droidconke19.datastates.FirebaseResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun <T, R : Any> T.runCatching(
        block: suspend T.() -> R
): FirebaseResult<R> = withContext(Dispatchers.IO) {
    return@withContext try {
        FirebaseResult.Success(block())
    } catch (e: Exception) {
        FirebaseResult.Error(e.message)
    }
}