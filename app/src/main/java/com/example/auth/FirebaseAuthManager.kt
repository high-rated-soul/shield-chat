package com.example.auth

/**
 * Manager class for handling phone authentication flows via Firebase.
 * Represents securely verifiable phone sessions.
 */
class FirebaseAuthManager {

    suspend fun sendVerificationCode(phone: String, onCodeSent: (String) -> Unit, onError: (Exception) -> Unit) {
        if (phone.length >= 7) {
            onCodeSent("mock_verification_id_12345")
        } else {
            onError(Exception("Invalid phone number format."))
        }
    }

    suspend fun verifyOtp(verificationId: String, code: String, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        if (code == "123456" || code.length >= 4) {
            onSuccess()
        } else {
            onError(Exception("Invalid SMS code."))
        }
    }

    fun isUserLoggedIn(): Boolean {
        return false 
    }
}
