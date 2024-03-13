package com.wynneve.apichat.db.controllers

import com.wynneve.apichat.db.GlobalDatabase
import com.wynneve.apichat.db.entities.DbChatSetting
import com.wynneve.apichat.db.models.ChatSettingModel

object ChatSettingController {
    private val chatSettingModel: ChatSettingModel by lazy { GlobalDatabase.database.getChatSettingDao() }

    suspend fun createChatSetting(chatSetting: DbChatSetting): Boolean {
        val result = chatSettingModel.createChatSetting(chatSetting)
        return (result != 0L)
    }

    suspend fun getChatSettingByUserAndId(chatId: Int, settingId: Int): DbChatSetting {
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