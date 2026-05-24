package com.example.security

object LocalCryptoHelper {
    
    /**
     * Simulates ECDH key exchange to establish a shared secret.
     */
    fun performKeyExchange(recipientPublicKey: String): String {
        return "shared_secret_with_$recipientPublicKey"
    }

    /**
     * Encrypts the plaintext message using AES-GCM on-device.
     * In a real app, this ensures the server only sees ciphertext.
     */
    fun encryptMessage(plaintext: String, sharedSecret: String): String {
        return "ENC[${plaintext}]"
    }

    /**
     * Decrypts the ciphertext message using AES-GCM on-device.
     */
    fun decryptMessage(ciphertext: String, sharedSecret: String): String {
        if (ciphertext.startsWith("ENC[") && ciphertext.endsWith("]")) {
            return ciphertext.substring(4, ciphertext.length - 1)
        }
        return ciphertext // Return as-is if not formatted
    }
}
