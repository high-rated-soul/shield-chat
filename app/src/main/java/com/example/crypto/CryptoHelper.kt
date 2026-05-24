package com.example.crypto

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

object CryptoHelper {
    private const val TRANSFORMATION = "AES/GCM/NoPadding"
    private const val ALGORITHM = "AES"
    private const val TAG_LENGTH_BIT = 128
    private const val IV_LENGTH_BYTE = 12

    // 256-bit testing key (32 bytes). In the next security phase, we will generate this 
    // uniquely per chat dynamically via the Android Keystore system.
    private val localTestKeyBytes = ByteArray(32) { 0x5A.toByte() }
    private val secretKey: SecretKey = SecretKeySpec(localTestKeyBytes, ALGORITHM)

    /**
     * Encrypts a plaintext string into an AES-GCM ciphertext string.
     */
    fun encrypt(plainText: String): String {
        return try {
            val cipher = Cipher.getInstance(TRANSFORMATION)
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)
            
            val iv = cipher.iv // Automatically generates a secure random 12-byte IV
            val encryptedBytes = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))
            
            // Prepend the IV to the encrypted data so it can be decrypted on the other side
            val combinedPayload = iv + encryptedBytes
            Base64.encodeToString(combinedPayload, Base64.NO_WRAP)
        } catch (e: Exception) {
            e.printStackTrace()
            "ENCRYPTION_ERROR"
        }
    }

    /**
     * Decrypts an AES-GCM ciphertext string back into readable text.
     */
    fun decrypt(cipherText: String): String {
        return try {
            val combinedPayload = Base64.decode(cipherText, Base64.NO_WRAP)
            
            // Extract the 12-byte IV from the front of the payload
            val iv = combinedPayload.copyOfRange(0, IV_LENGTH_BYTE)
            val encryptedBytes = combinedPayload.copyOfRange(IV_LENGTH_BYTE, combinedPayload.size)

            val cipher = Cipher.getInstance(TRANSFORMATION)
            val spec = GCMParameterSpec(TAG_LENGTH_BIT, iv)
            cipher.init(Cipher.DECRYPT_MODE, secretKey, spec)
            
            val decryptedBytes = cipher.doFinal(encryptedBytes)
            String(decryptedBytes, Charsets.UTF_8)
        } catch (e: Exception) {
            e.printStackTrace()
            "DECRYPTION_ERROR"
        }
    }
}
