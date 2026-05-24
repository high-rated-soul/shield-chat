package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.auth.FirebaseAuthManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class CodeSent(val verificationId: String, val message: String) : AuthState()
    object Authenticated : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel : ViewModel() {
    private val authManager = FirebaseAuthManager()
    
    private val _authState = MutableStateFlow<AuthState>(if (authManager.isUserLoggedIn()) AuthState.Authenticated else AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    fun requestPhoneCode(phoneNumber: String) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            // Simulate network latency for Firebase initialization
            delay(1000)
            authManager.sendVerificationCode(
                phone = phoneNumber,
                onCodeSent = { verificationId ->
                    _authState.value = AuthState.CodeSent(verificationId, "SMS code sent to $phoneNumber")
                },
                onError = {
                    _authState.value = AuthState.Error(it.message ?: "Authentication failed")
                }
            )
        }
    }

    fun verifyCode(verificationId: String, smsCode: String) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            delay(1200) // Simulate Firebase handshake
            authManager.verifyOtp(
                verificationId = verificationId,
                code = smsCode,
                onSuccess = {
                    _authState.value = AuthState.Authenticated
                },
                onError = {
                    _authState.value = AuthState.Error(it.message ?: "Invalid Code")
                }
            )
        }
    }
    
    fun resetState() {
        _authState.value = AuthState.Idle
    }
}
