package com.wynneve.apichat.db.models

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.wynneve.apichat.db.entities.DbChatSetting

@Dao
interface ChatSettingModel {
    // indexation implies uniqueness
    @Query("SELECT chat_id, setting_id, value FROM chat_settings WHERE chat_id = :chatId AND setting_id = :settingId")
    suspend fun getChatSettingByChatAndId(chatId: Int, settingId: Int): DbChatSetting

    @Query("SELECT chat_id, setting_id, value FROM chat_settings WHERE chat_id = :chatId")
    suspend fun getChatSettingsByChat(chatId: Int): Array<DbChatSetting>

    @Insert(entity = DbChatSetting::class)
    suspend fun createChatSetting(chatSetting: DbChatSetting): Long

    @Update(entity = DbChatSetting::class)
    suspend fun updateChatSetting(chatSetting: DbChatSetting): Int

    @Query("DELETE FROM chat_settings WHERE chat_id = :chatId AND setting_id = :settingId")
    suspend fun deleteChatSettingByChatAndId(chatId: Int, settingId: Int): Int

    @Query("DELETE FROM global_settings WHERE user_id = :chatId")
    suspend fun deleteChatSettingsByChat(chatId: Int): Int
}