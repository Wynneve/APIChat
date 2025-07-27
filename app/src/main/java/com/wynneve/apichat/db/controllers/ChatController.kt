package com.wynneve.apichat.db.controllers

import com.wynneve.apichat.db.ApplicationDatabase
import com.wynneve.apichat.db.entities.DbChat
import com.wynneve.apichat.db.models.ChatModel
import kotlinx.coroutines.flow.Flow

object ChatController {
    private val chatModel: ChatModel by lazy { ApplicationDatabase.database.getChatDao() }

    suspend fun createChat(chat: DbChat): Long {
        return chatModel.createChat(chat)
    }

    fun getChatById(id: Int): Flow<DbChat?> {
        return chatModel.getChatById(id)
    }

    fun getChatsByProfile(profileId: Int): Flow<List<DbChat>> {
        return chatModel.getChatsByProfile(profileId)
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