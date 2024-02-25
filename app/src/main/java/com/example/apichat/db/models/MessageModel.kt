package com.example.apichat.db.models

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.apichat.db.entities.DbMessage

@Dao
interface MessageModel {
    // the login field is indexed, thus there shall be a unique user
    @Query("SELECT id, chat_id, role, timestamp, content FROM messages WHERE id = :id")
    suspend fun getMessageById(id: String): DbMessage

    @Query("SELECT id, chat_id, role, timestamp, content FROM messages WHERE chat_id = :chatId")
    suspend fun getMessagesByChat(chatId: Int): Array<DbMessage>

    @Insert(entity = DbMessage::class)
    suspend fun createUser(message: DbMessage): Long

    @Update(entity = DbMessage::class)
    suspend fun updateMessage(message: DbMessage): Int

    @Query("DELETE FROM messages WHERE id = :id")
    suspend fun deleteMessageById(id: Int): Int
}