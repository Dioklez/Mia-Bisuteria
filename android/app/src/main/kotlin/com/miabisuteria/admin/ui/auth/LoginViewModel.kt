package com.miabisuteri.admin.ui.auth

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.lifecycle.ViewModel
import com.miabisuteri.admin.util.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class LoginUiState(
    val password: String = "",
    val pin: String = "",
    val showPin: Boolean = false,
    val error: String? = null,
    val loginSuccess: Boolean = false,
    val isFirstSetup: Boolean = false
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState(
        isFirstSetup = !sessionManager.isPasswordSet(),
        showPin = sessionManager.isPinSet() && sessionManager.isPasswordSet()
    ))
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onPasswordChange(value: String) {
        _uiState.update { it.copy(password = value, error = null) }
    }

    fun onPinChange(value: String) {
        _uiState.update { it.copy(pin = value, error = null) }
    }

    fun onPasswordSubmit() {
        val password = _uiState.value.password
        if (password.isBlank()) {
            _uiState.update { it.copy(error = "Ingresá una contraseña") }
            return
        }

        if (_uiState.value.isFirstSetup) {
            if (password.length < 4) {
                _uiState.update { it.copy(error = "Mínimo 4 caracteres") }
                return
            }
            sessionManager.setPassword(password)
            sessionManager.login()
            _uiState.update { it.copy(loginSuccess = true) }
        } else {
            if (sessionManager.checkPassword(password)) {
                sessionManager.login()
                _uiState.update { it.copy(loginSuccess = true) }
            } else {
                _uiState.update { it.copy(error = "Contraseña incorrecta", password = "") }
            }
        }
    }

    fun onPinSubmit() {
        val pin = _uiState.value.pin
        if (sessionManager.checkPin(pin)) {
            sessionManager.login()
            _uiState.update { it.copy(loginSuccess = true) }
        } else {
            _uiState.update { it.copy(error = "PIN incorrecto", pin = "") }
        }
    }

    fun onSwitchToPassword() {
        _uiState.update { it.copy(showPin = false, pin = "", error = null) }
    }

    fun onBiometricSuccess() {
        sessionManager.login()
        _uiState.update { it.copy(loginSuccess = true) }
    }

    fun isBiometricAvailable(context: Context): Boolean {
        val bm = BiometricManager.from(context)
        return bm.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK) ==
                BiometricManager.BIOMETRIC_SUCCESS
    }

    fun isBiometricEnabled(): Boolean = sessionManager.isBiometricEnabled()
}
