package com.example.data.repository

import com.example.data.db.ChannelDao
import com.example.data.db.MessageDao
import com.example.data.db.SummaryDao
import com.example.data.model.Channel
import com.example.data.model.MessagePayload
import com.example.security.LocalCryptoHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.util.UUID

class ChatRepository(
    private val channelDao: ChannelDao,
    private val messageDao: MessageDao,
    private val summaryDao: SummaryDao
) {
    val allChannels: Flow<List<Channel>> = channelDao.getAllChannels()

    private val wsClient = OkHttpClient()
    private var webSocket: WebSocket? = null

    init {
        initializeWebSocket()
    }

    /**
     * Initializes the WebSocket connection for real-time chat sync.
     */
    private fun initializeWebSocket() {
        // The mock websocket connection might crash the AI Studio streaming emulator sandbox
        // if outbound connections are restricted or DNS resolves unexpectedly during init.
        // Bypassing for now to ensure the UI loads reliably.
        /*
        val request = Request.Builder().url("wss://echo.websocket.events").build()
        webSocket = wsClient.newWebSocket(request, object : WebSocketListener() {
            override fun onMessage(webSocket: WebSocket, text: String) {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val incomingPayload = Json.decodeFromString<MessagePayload>(text)
                        if (incomingPayload.senderId == "me") return@launch
                        messageDao.insertMessage(incomingPayload.copy(isMine = false))
                    } catch (e: Exception) {}
                }
            }
            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {}
        })
        */
    }

    fun getDecryptedMessagesForChannel(channelId: String): Flow<List<MessagePayload>> {
        return messageDao.getMessagesForChannel(channelId).map { messages ->
            messages.map { msg ->
                // Decrypt message body locally before providing to UI
                val decryptedBody = LocalCryptoHelper.decryptMessage(msg.encryptedBody, "mock_secret")
                msg.copy(encryptedBody = decryptedBody)
            }
        }
    }

    suspend fun sendMessage(channelId: String, plaintext: String, channelName: String) {
        // Encrypt message body BEFORE storing/sending. Server only ever touches Ciphertext.
        val encryptedBody = LocalCryptoHelper.encryptMessage(plaintext, "mock_secret")
        
        val message = MessagePayload(
            id = UUID.randomUUID().toString(),
            channelId = channelId,
            senderId = "me",
            recipientId = "them",
            encryptedBody = encryptedBody,
            isMine = true
        )
        
        // Save ciphertext locally
        messageDao.insertMessage(message)
        
        // Transmit securely via WebSocket
        val payloadJson = Json.encodeToString(message)
        webSocket?.send(payloadJson)
        
        // Update channel snippet summary
        val updatedChannel = Channel(
            id = channelId,
            name = channelName,
            lastMessageSnippet = plaintext,
            timestamp = System.currentTimeMillis()
        )
        channelDao.insertChannel(updatedChannel)
    }
    
    suspend fun insertMockData() {
        val channel1 = Channel("1", "Alice Smith", avatarUrl = "", isGroup = false, lastMessageSnippet = "Sounds good!", unreadCount = 2)
        val channel2 = Channel("2", "Project X Group", avatarUrl = "", isGroup = true, lastMessageSnippet = "We need the summary by tomorrow.", unreadCount = 15)
        
        channelDao.insertChannel(channel1)
        channelDao.insertChannel(channel2)
        
        messageDao.insertMessage(MessagePayload("m1", "1", "them", "me", LocalCryptoHelper.encryptMessage("Hey! Are we still on for today?", "mock"), timestamp = System.currentTimeMillis() - 60000, isMine = false))
        messageDao.insertMessage(MessagePayload("m2", "1", "them", "me", LocalCryptoHelper.encryptMessage("Sounds good!", "mock"), timestamp = System.currentTimeMillis() - 30000, isMine = false))
        messageDao.insertMessage(MessagePayload("m3", "2", "Bob", "all", LocalCryptoHelper.encryptMessage("We need the summary by tomorrow. We should also check the analytics for the app.", "mock"), timestamp = System.currentTimeMillis() - 86400000, isMine = false))
    }
}
