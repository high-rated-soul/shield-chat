package com.example.data.repository

import com.example.ai.LocalLlmService
import com.example.data.db.MessageDao
import com.example.data.db.SummaryDao
import com.example.data.model.SmartSummary
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SummaryRepository(
    private val messageDao: MessageDao,
    private val summaryDao: SummaryDao,
    private val localLlmService: LocalLlmService = LocalLlmService()
) {
    val allSummaries: Flow<List<SmartSummary>> = summaryDao.getAllSummaries()

    suspend fun generateSummaryLocally(channelId: String): String = withContext(Dispatchers.IO) {
        // Query encrypted messages from the local SQLite database
        val unreadMessages = messageDao.getRecentUnreadMessages(channelId)
        val encryptedPayloads = unreadMessages.map { it.encryptedBody }
        
        if (encryptedPayloads.isEmpty()) return@withContext "No new messages to summarize."
        
        // Mandate local hardware-only processing pipeline
        val summaryText = localLlmService.processAndSummarize(encryptedPayloads, "mock_secret")
        
        // Store the AI-generated result locally
        summaryDao.insertSummary(SmartSummary(channelId, summaryText))
        
        return@withContext summaryText
    }
}
