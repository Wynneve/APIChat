package com.wynneve.apichat.db.models

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.wynneve.apichat.db.entities.DbChat

@Dao
interface ChatModel {
    @Query("SELECT id, user_id, title, last_access FROM chats WHERE id = :id")
    suspend fun getChatById(id: Int): DbChat

    @Query("SELECT id, user_id, title, last_access FROM chats WHERE user_id = :userId")
    suspend fun getChatsByUser(userId: Int): Array<DbChat>

    @Insert(entity = DbChat::class)
    suspend fun createChat(chat: DbChat): Long

    @Update(entity = DbChat::class)
    suspend fun updateChat(chat: DbChat): Int

    @Query("DELETE FROM chats WHERE id = :id")
    suspend fun deleteChatById(id: Int): Int
}