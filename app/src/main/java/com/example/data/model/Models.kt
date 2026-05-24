package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "channels")
data class Channel(
    @PrimaryKey val id: String,
    val name: String,
    val avatarUrl: String = "",
    val isGroup: Boolean = false,
    val isPublic: Boolean = false,
    val unreadCount: Int = 0,
    val lastMessageSnippet: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

@Serializable
@Entity(tableName = "messages")
data class MessagePayload(
    @PrimaryKey val id: String,
    val channelId: String,
    val senderId: String,
    val recipientId: String,
    val encryptedBody: String, // Ciphertext in DB
    val timestamp: Long = System.currentTimeMillis(),
    val isMine: Boolean = false
)

@Serializable
@Entity(tableName = "summaries")
data class SmartSummary(
    @PrimaryKey val channelId: String,
    val summaryText: String,
    val generatedAt: Long = System.currentTimeMillis()
)
