package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel : ViewModel() {
    private val _isFinishedLoading = MutableStateFlow(false)
    val isFinishedLoading: StateFlow<Boolean> = _isFinishedLoading

    private val _verificationState = MutableStateFlow<String?>(null)
    val verificationState: StateFlow<String?> = _verificationState

    fun sendVerificationCode(phoneNumber: String, onCodeSent: (String) -> Unit) {
        // This is where Firebase Phone Auth will plug in later
        val mockVerificationId = "mock_id_${System.currentTimeMillis()}"
        _verificationState.value = mockVerificationId
        onCodeSent(mockVerificationId)
    }

    fun verifyOtp(code: String, onSuccess: () -> Unit) {
        // This is where code verification verification completes
        if (code.length >= 4) {
            _isFinishedLoading.value = true
            onSuccess()
        }
    }
}
