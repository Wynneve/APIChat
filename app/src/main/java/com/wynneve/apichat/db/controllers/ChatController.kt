package com.wynneve.apichat.db.controllers

import com.wynneve.apichat.db.GlobalDatabase
import com.wynneve.apichat.db.entities.DbChat
import com.wynneve.apichat.db.models.ChatModel

object ChatController {
    private val chatModel: ChatModel by lazy { GlobalDatabase.database.getChatDao() }

    suspend fun createChat(chat: DbChat): Boolean {
        val result = chatModel.createChat(chat)
        return (result != 0L)
    }

    suspend fun getChatById(id: Int): DbChat {
        return chatModel.getChatById(id)
    }

    suspend fun getChatsByUser(userId: Int): Array<DbChat> {
        return chatModel.getChatsByUser(userId)
    }

    suspend fun updateChat(chat: DbChat): Boolean {
        val result = chatModel.updateChat(chat)
        return (result != 0)
    }

    suspend fun deleteChatById(id: Int): Boolean {
        val result = chatModel.deleteChatById(id)
        return (result != 0)
    }
}