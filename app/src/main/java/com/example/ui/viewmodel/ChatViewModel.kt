package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.AppDatabase
import com.example.data.model.Channel
import com.example.data.model.MessagePayload
import com.example.data.model.SmartSummary
import com.example.data.repository.ChatRepository
import com.example.data.repository.SummaryRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ChatViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val chatRepo = ChatRepository(db.channelDao(), db.messageDao(), db.summaryDao())
    private val summaryRepo = SummaryRepository(db.messageDao(), db.summaryDao())

    val channels: StateFlow<List<Channel>> = chatRepo.allChannels.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
    
    val summaries: StateFlow<List<SmartSummary>> = summaryRepo.allSummaries.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        viewModelScope.launch {
            chatRepo.insertMockData()
        }
    }

    fun getMessagesForChannel(channelId: String): StateFlow<List<MessagePayload>> {
        return chatRepo.getDecryptedMessagesForChannel(channelId).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }

    fun sendMessage(channelId: String, text: String, channelName: String) {
        viewModelScope.launch {
            chatRepo.sendMessage(channelId, text, channelName)
        }
    }

    fun generateSummary(channelId: String) {
        viewModelScope.launch {
            summaryRepo.generateSummaryLocally(channelId)
        }
    }
}
