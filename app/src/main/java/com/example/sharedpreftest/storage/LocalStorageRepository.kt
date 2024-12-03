package com.example.sharedpreftest.storage

import java.time.ZonedDateTime
import kotlin.coroutines.CoroutineContext

interface LocalStorageRepository {
    suspend fun saveLocalDateTime(
        key: String,
        localDateTime: ZonedDateTime,
        coroutineContext: CoroutineContext? = null
    )
    suspend fun getLocalDateTime(
        key: String,
        coroutineContext: CoroutineContext? = null
    ): ZonedDateTime?
    suspend fun saveBoolean(
        key: String,
        boolean: Boolean,
        coroutineContext: CoroutineContext? = null
    )
    suspend fun getBoolean(key: String, coroutineContext: CoroutineContext? = null): Boolean?
    suspend fun saveDouble(key: String, double: Double, coroutineContext: CoroutineContext? = null)
    suspend fun getDouble(key: String, coroutineContext: CoroutineContext? = null): Double?
    suspend fun saveLong(key: String, long: Long, coroutineContext: CoroutineContext? = null)
    suspend fun getLong(key: String, coroutineContext: CoroutineContext? = null): Long?
    suspend fun saveString(key: String, string: String, coroutineContext: CoroutineContext? = null)
    suspend fun getString(key: String, coroutineContext: CoroutineContext? = null): String?
    suspend fun hasKey(key: String, coroutineContext: CoroutineContext? = null): Boolean
    suspend fun removeValueByKey(key: String, coroutineContext: CoroutineContext? = null)
}
