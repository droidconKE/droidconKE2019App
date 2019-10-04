package com.android254.droidconke19.datastates

suspend fun <T, R : Any> T.runCatching(block: suspend T.() -> Result<R>) = try {
    block()
} catch (e: Exception) {
    Result.Error(e.message)
}