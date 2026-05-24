package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class BackendMessage(
    val id: String = "",
    val encryptedPayload: String = "",
    val senderId: String = "",
    val isFromMe: Boolean = false,
    val timestamp: Long = 0L
)

class ChatViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val _messages = MutableStateFlow<List<BackendMessage>>(emptyList())
    val messages: StateFlow<List<BackendMessage>> = _messages

    /**
     * Pushes the AES-GCM encrypted ciphertext safely up to the Firebase cloud matrix.
     */
    fun sendMessageToFirebase(channelId: String, ciphertext: String) {
        val messageData = hashMapOf(
            "encryptedPayload" to ciphertext,
            "senderId" to "user_placeholder", // Will bind to active Firebase Auth UID later
            "timestamp" to System.currentTimeMillis()
        )
        
        db.collection("channels")
            .document(channelId)
            .collection("messages")
            .add(messageData)
    }

    /**
     * Establishes a live websocket pipeline to stream new incoming encrypted chats instantly.
     */
    fun listenToChannel(channelId: String) {
        db.collection("channels")
            .document(channelId)
            .collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener
                
                val fetchedMessages = snapshot.documents.mapNotNull { doc ->
                    val payload = doc.getString("encryptedPayload") ?: ""
                    val sender = doc.getString("senderId") ?: ""
                    val time = doc.getLong("timestamp") ?: 0L
                    
                    BackendMessage(
                        id = doc.id,
                        encryptedPayload = payload,
                        senderId = sender,
                        isFromMe = sender == "user_placeholder",
                        timestamp = time
                    )
                }
                _messages.value = fetchedMessages
            }
    }
}
