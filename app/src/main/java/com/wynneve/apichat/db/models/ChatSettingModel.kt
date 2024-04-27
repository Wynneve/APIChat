package com.wynneve.apichat.db.models

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.wynneve.apichat.db.entities.DbChatSetting
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatSettingModel {
    // indexation implies uniqueness
    @Query("SELECT chat_id, setting_id, value FROM chat_settings WHERE chat_id = :chatId AND setting_id = :settingId")
    fun getChatSettingByChatAndId(chatId: Int, settingId: Int): Flow<DbChatSetting?>

    @Query("SELECT chat_id, setting_id, value FROM chat_settings WHERE chat_id = :chatId")
    fun getChatSettingsByChat(chatId: Int): Flow<List<DbChatSetting>>

    @Insert(entity = DbChatSetting::class)
    suspend fun createChatSetting(chatSetting: DbChatSetting): Long

    @Update(entity = DbChatSetting::class)
    suspend fun updateChatSetting(chatSetting: DbChatSetting): Int

    @Query("DELETE FROM chat_settings WHERE chat_id = :chatId AND setting_id = :settingId")
    suspend fun deleteChatSettingByChatAndId(chatId: Int, settingId: Int): Int

    @Query("DELETE FROM global_settings WHERE profile_id = :chatId")
    suspend fun deleteChatSettingsByChat(chatId: Int): Int
}