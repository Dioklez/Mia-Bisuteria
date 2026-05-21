package com.miabisuteri.admin.util

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.security.MessageDigest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val prefs = EncryptedSharedPreferences.create(
        context,
        "mia_secure_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    private val _isLoggedIn = MutableStateFlow(checkSession())
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private var lastActivityMs: Long = System.currentTimeMillis()
    private val sessionTimeoutMs = 10 * 60 * 1000L // 10 minutes

    private fun checkSession(): Boolean {
        val lastMs = prefs.getLong(KEY_LAST_ACTIVITY, 0L)
        if (lastMs == 0L) return false
        return (System.currentTimeMillis() - lastMs) < sessionTimeoutMs
    }

    fun onAppForegrounded() {
        if (_isLoggedIn.value) {
            val elapsed = System.currentTimeMillis() - lastActivityMs
            if (elapsed > sessionTimeoutMs) {
                _isLoggedIn.value = false
                prefs.edit().putLong(KEY_LAST_ACTIVITY, 0L).apply()
            } else {
                refreshSession()
            }
        }
    }

    fun refreshSession() {
        lastActivityMs = System.currentTimeMillis()
        prefs.edit().putLong(KEY_LAST_ACTIVITY, lastActivityMs).apply()
    }

    fun login() {
        refreshSession()
        _isLoggedIn.value = true
    }

    fun logout() {
        prefs.edit().putLong(KEY_LAST_ACTIVITY, 0L).apply()
        _isLoggedIn.value = false
    }

    // Password management
    fun isPasswordSet(): Boolean = prefs.getString(KEY_PASSWORD_HASH, null) != null

    fun setPassword(password: String) {
        prefs.edit().putString(KEY_PASSWORD_HASH, hash(password)).apply()
    }

    fun checkPassword(password: String): Boolean {
        val stored = prefs.getString(KEY_PASSWORD_HASH, null) ?: return false
        return stored == hash(password)
    }

    // PIN management
    fun isPinSet(): Boolean = prefs.getString(KEY_PIN_HASH, null) != null

    fun setPin(pin: String) {
        prefs.edit().putString(KEY_PIN_HASH, hash(pin)).apply()
    }

    fun checkPin(pin: String): Boolean {
        val stored = prefs.getString(KEY_PIN_HASH, null) ?: return false
        return stored == hash(pin)
    }

    fun isBiometricEnabled(): Boolean = prefs.getBoolean(KEY_BIOMETRIC, false)

    fun setBiometricEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_BIOMETRIC, enabled).apply()
    }

    private fun hash(input: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    companion object {
        private const val KEY_PASSWORD_HASH = "password_hash"
        private const val KEY_PIN_HASH = "pin_hash"
        private const val KEY_BIOMETRIC = "biometric_enabled"
        private const val KEY_LAST_ACTIVITY = "last_activity_ms"
    }
}
