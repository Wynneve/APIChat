package com.wynneve.apichat.db.models

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.wynneve.apichat.db.entities.DbMessage
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageModel {
    // the login field is indexed, thus there shall be a unique user
    @Query("SELECT id, chat_id, role, timestamp, content, image FROM messages WHERE id = :id")
    fun getMessageById(id: Int): Flow<DbMessage?>

    @Query("SELECT id, chat_id, role, timestamp, content, image FROM messages WHERE chat_id = :chatId")
    fun getMessagesByChat(chatId: Int): Flow<List<DbMessage>>

    @Insert(entity = DbMessage::class)
    suspend fun createMessage(message: DbMessage): Long

    @Update(entity = DbMessage::class)
    suspend fun updateMessage(message: DbMessage): Int

    @Query("DELETE FROM messages WHERE id = :id")
    suspend fun deleteMessageById(id: Int): Int

    @Query("DELETE FROM messages WHERE chat_id = :chatId")
    suspend fun deleteMessagesByChat(chatId: Int): Int
}