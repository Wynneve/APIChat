package com.wynneve.apichat.db.models

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.wynneve.apichat.db.entities.DbChat
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatModel {
    @Query("SELECT id, profile_id, title, last_access FROM chats WHERE id = :id")
    fun getChatById(id: Int): Flow<DbChat?>

    @Query("SELECT id, profile_id, title, last_access FROM chats WHERE profile_id = :profileId")
    fun getChatsByProfile(profileId: Int): Flow<List<DbChat>>

//    @Query("""SELECT chats.id, chats.profile_id, chats.title, chats.last_access
//        FROM chats INNER JOIN profiles ON chats.profile_id = profiles.id
//        WHERE profiles.login = :profileLogin""")
//    suspend fun getChatsByProfileLogin(profileLogin: String): Array<DbChat>

    @Insert(entity = DbChat::class)
    suspend fun createChat(chat: DbChat): Long

    @Update(entity = DbChat::class)
    suspend fun updateChat(chat: DbChat): Int

    @Query("DELETE FROM chats WHERE id = :id")
    suspend fun deleteChatById(id: Int): Int
}