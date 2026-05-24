package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.Channel
import com.example.data.model.MessagePayload
import com.example.data.model.SmartSummary
import kotlinx.coroutines.flow.Flow

@Dao
interface ChannelDao {
    @Query("SELECT * FROM channels ORDER BY timestamp DESC")
    fun getAllChannels(): Flow<List<Channel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChannel(channel: Channel)
}

@Dao
interface MessageDao {
    @Query("SELECT * FROM messages WHERE channelId = :channelId ORDER BY timestamp ASC")
    fun getMessagesForChannel(channelId: String): Flow<List<MessagePayload>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessagePayload)
    
    @Query("SELECT * FROM messages WHERE isMine = 0 AND channelId = :channelId ORDER BY timestamp DESC LIMIT 50")
    suspend fun getRecentUnreadMessages(channelId: String): List<MessagePayload>
}

@Dao
interface SummaryDao {
    @Query("SELECT * FROM summaries")
    fun getAllSummaries(): Flow<List<SmartSummary>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSummary(summary: SmartSummary)
}
