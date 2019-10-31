package com.android254.droidconke19.datastates

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun <T, R : Any> T.runCatching(
        block: suspend T.() -> R
): Result<R> = withContext(Dispatchers.IO) {
    return@withContext try {
        Result.Success(block())
    } catch (e: Exception) {
        Result.Error(e.message)
    }
}