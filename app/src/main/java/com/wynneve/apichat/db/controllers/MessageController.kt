package com.wynneve.apichat.db.controllers

import com.wynneve.apichat.db.ApplicationDatabase
import com.wynneve.apichat.db.entities.DbMessage
import com.wynneve.apichat.db.models.MessageModel
import kotlinx.coroutines.flow.Flow

object MessageController {
    private val messageModel: MessageModel by lazy { ApplicationDatabase.database.getMessageDao() }

    suspend fun createMessage(message: DbMessage): Boolean {
        val result = messageModel.createMessage(message)
        return (result != 0L)
    }

    fun getMessageById(id: Int): Flow<DbMessage?> {
        return messageModel.getMessageById(id)
    }

    fun getMessagesByChat(chatId: Int): Flow<List<DbMessage>> {
        return messageModel.getMessagesByChat(chatId)
    }

    suspend fun updateMessage(message: DbMessage): Boolean {
        val result = messageModel.updateMessage(message)
        return (result != 0)
    }

    suspend fun deleteMessageById(id: Int): Boolean {
        val result = messageModel.deleteMessageById(id)
        return (result != 0)
    }

    suspend fun deleteMessagesByChat(chatId: Int): Boolean {
        val result = messageModel.deleteMessagesByChat(chatId)
        return (result != 0)
    }
}