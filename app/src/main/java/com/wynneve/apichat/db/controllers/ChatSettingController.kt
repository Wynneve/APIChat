package com.wynneve.apichat.db.controllers

import com.wynneve.apichat.db.ApplicationDatabase
import com.wynneve.apichat.db.entities.DbChatSetting
import com.wynneve.apichat.db.models.ChatSettingModel
import kotlinx.coroutines.flow.Flow

object ChatSettingController {
    private val chatSettingModel: ChatSettingModel by lazy { ApplicationDatabase.database.getChatSettingDao() }

    suspend fun createChatSetting(chatSetting: DbChatSetting): Boolean {
        val result = chatSettingModel.createChatSetting(chatSetting)
        return (result != 0L)
    }

    fun getChatSettingByUserAndId(chatId: Int, settingId: Int): Flow<DbChatSetting?> {
        return chatSettingModel.getChatSettingByChatAndId(chatId, settingId)
    }

    suspend fun updateChatSetting(chatSetting: DbChatSetting): Boolean {
        val result = chatSettingModel.updateChatSetting(chatSetting)
        return (result != 0)
    }

    suspend fun deleteChatSettingByUserAndId(chatId: Int, settingId: Int): Boolean {
        val result = chatSettingModel.deleteChatSettingByChatAndId(chatId, settingId)
        return (result != 0)
    }
}