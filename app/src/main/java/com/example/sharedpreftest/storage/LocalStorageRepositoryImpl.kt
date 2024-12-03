package com.example.sharedpreftest.storage

import android.content.SharedPreferences
import android.util.Log
import com.example.sharedpreftest.coroutines.WellDispatcher
import com.example.sharedpreftest.extensions.toEpochMillis
import com.example.sharedpreftest.extensions.toLocalDateTime
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.time.ZonedDateTime
import kotlin.coroutines.CoroutineContext
import kotlin.system.measureTimeMillis

const val LOCAL_STORAGE_APP_REVIEW_DATE_TIME = "LOCAL_STORAGE_APP_REVIEW_DATE_TIME"
const val FEATURE_FLAG_OVERRIDE = "FEATURE_FLAG_OVERRIDE"
const val PREF_CURRENT_REGISTRATION_STATE = "pref_currentRegistrationStateStep"
const val HAS_LOGGED_IN_BEFORE = "confirmed"
const val PREF_DEEPLINK = "PREF_DEEPLINK"
const val PREF_BIOMETRIC_SAVED_PASSWORD = "pref_biometricPasswordLoginRequired"
const val PREF_SAVED_USERNAME = "pref_savedUsernameRequired"
const val PREF_REMEMBER_ME = "pref_rememberMe"
const val FIREBASE_TOKEN_PREF = "pref_firebase_token"
const val PREF_DB_PASS_PHRASE = "pref_database_pass_phrase"

class LocalStorageRepositoryImpl(
    private var preferences: SharedPreferences,
    private val wellDispatcher: WellDispatcher,
) : LocalStorageRepository {
    override suspend fun saveLocalDateTime(
        key: String,
        localDateTime: ZonedDateTime,
        coroutineContext: CoroutineContext?,
    ) {
        withContext(coroutineContext ?: wellDispatcher.io()) {
            preferences.editAndApply { it.putLong(key, localDateTime.toEpochMillis()) }
        }
    }

    override suspend fun getLocalDateTime(
        key: String,
        coroutineContext: CoroutineContext?,
    ): ZonedDateTime? =
        withContext(coroutineContext ?: wellDispatcher.io()) {
            if (preferences.contains(key)) {
                preferences.getLong(key, -1L).toLocalDateTime()
            } else {
                null
            }
        }

    override suspend fun saveBoolean(
        key: String,
        boolean: Boolean,
        coroutineContext: CoroutineContext?,
    ) {
        withContext(coroutineContext ?: wellDispatcher.io()) {
            preferences.editAndApply { it.putBoolean(key, boolean) }
        }
    }

    override suspend fun getBoolean(
        key: String,
        coroutineContext: CoroutineContext?,
    ): Boolean? =
        withContext(coroutineContext ?: wellDispatcher.io()) {
            if (preferences.contains(key)) {
                preferences.getBoolean(key, false)
            } else {
                null
            }
        }

    override suspend fun saveDouble(
        key: String,
        double: Double,
        coroutineContext: CoroutineContext?,
    ) {
        withContext(coroutineContext ?: wellDispatcher.io()) {
            preferences.editAndApply { it.putLong(key, double.toBits()) }
        }
    }

    override suspend fun getDouble(
        key: String,
        coroutineContext: CoroutineContext?,
    ): Double? =
        withContext(coroutineContext ?: wellDispatcher.io()) {
            if (preferences.contains(key)) {
                Double.fromBits(preferences.getLong(key, 0))
            } else {
                null
            }
        }

    override suspend fun saveLong(
        key: String,
        long: Long,
        coroutineContext: CoroutineContext?,
    ) {
        withContext(coroutineContext ?: wellDispatcher.io()) {
            preferences.editAndApply { it.putLong(key, long) }
        }
    }

    override suspend fun getLong(
        key: String,
        coroutineContext: CoroutineContext?,
    ): Long? =
        withContext(coroutineContext ?: wellDispatcher.io()) {
            if (preferences.contains(key)) {
                preferences.getLong(key, -1L)
            } else {
                null
            }
        }

    override suspend fun saveString(
        key: String,
        string: String,
        coroutineContext: CoroutineContext?,
    ) {
        withContext(coroutineContext ?: wellDispatcher.io()) {
            preferences.editAndApply { it.putString(key, string) }
        }
    }

    override suspend fun getString(
        key: String,
        coroutineContext: CoroutineContext?,
    ): String? {
        suspend fun asd() =
            withContext(coroutineContext ?: wellDispatcher.io()) {
                if (preferences.contains(key)) {
                    preferences.getString(key, "")
                } else {
                    null
                }
            }

        var ss: String? = ""
        measureTimeMillis { ss = asd() }.also { Log.e("asd", "getString for $key took $it") }
        return ss
    }

    override suspend fun hasKey(
        key: String,
        coroutineContext: CoroutineContext?,
    ) = withContext(coroutineContext ?: wellDispatcher.io()) {
        preferences.contains(key)
    }

    override suspend fun removeValueByKey(
        key: String,
        coroutineContext: CoroutineContext?,
    ) {
        withContext(coroutineContext ?: wellDispatcher.io()) {
            preferences.edit().remove(key).apply()
        }
    }
}

private inline fun SharedPreferences.editAndApply(operation: (SharedPreferences.Editor) -> Unit) {
    val editor = this.edit()
    operation(editor)
    editor.apply()
}
