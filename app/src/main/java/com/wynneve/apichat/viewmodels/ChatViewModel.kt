package com.wynneve.apichat.viewmodels

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wynneve.apichat.ChatApiInstance
import com.wynneve.apichat.SendMessageResponse
import com.wynneve.apichat.db.SettingCategory
import com.wynneve.apichat.db.Settings
import com.wynneve.apichat.db.controllers.ChatSettingController
import com.wynneve.apichat.db.controllers.MessageController
import com.wynneve.apichat.db.controllers.SettingController
import com.wynneve.apichat.db.deserializeSetting
import com.wynneve.apichat.db.entities.DbChat
import com.wynneve.apichat.db.entities.DbMessage
import com.wynneve.apichat.prepareMessages
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

@Stable
class ChatViewModel(
    val chatId: Int,
    val navigateToSettings: () -> Unit,
    val navigateBack: () -> Unit,
    val loadChat: (callback: (DbChat) -> Unit) -> Unit,
    val saveChat: (chat: DbChat, callback: () -> Unit) -> Unit,
) : ViewModel() {
    private val _messages = MutableStateFlow<List<DbMessage>>(emptyList())
    val messages: StateFlow<List<DbMessage>> = _messages

    var chat: DbChat? = null
    var title by mutableStateOf("")

    var currentMessage by mutableStateOf("")
    var currentImage by mutableStateOf<String?>(null)
    var assistantIsTyping by mutableStateOf(false)

    var pickingSource by mutableStateOf(false)

    var editingMessage by mutableStateOf(0)
    var editingContent by mutableStateOf("")
    var editingImage by mutableStateOf<String?>(null)

    init {
        loadChat { chat ->
            this.chat = chat
            this.title = chat.title
        }
        observeMessages()
    }

    private fun observeMessages() {
        viewModelScope.launch {
            MessageController.getMessagesByChat(chatId).collect { messages ->
                _messages.emit(messages)
            }
        }
    }

    fun onDeleteClick() {
        viewModelScope.launch {
            MessageController.deleteMessageById(editingMessage)
            editingMessage = 0
            editingImage = null
        }
    }

    fun onSaveClick() {
        viewModelScope.launch {
            val message = MessageController.getMessageById(editingMessage).first()!!
            message.content = editingContent
            message.image = editingImage
            MessageController.updateMessage(message)

            editingMessage = 0
            editingImage = null
        }
    }

    fun onContinueClick() {
        viewModelScope.launch {
            assistantIsTyping = true

            val settings = Settings.values().associateWith { setting ->
                val chatSetting = ChatSettingController.getChatSettingByChatAndId(
                    chatId,
                    SettingController.settingsIds[setting]!!
                ).first()
                (chatSetting?.value ?: setting._default)
            }

            val request = prepareRequest(settings, true)
            sendMessageRequest(request, settings[Settings.ApiEndpoint]!!, true)
        }
    }

    fun onSendClick() {
        viewModelScope.launch {
            val message = DbMessage(
                chatId = chatId,
                role = "user",
                timestamp = Date().time,
                content = currentMessage,
                image = currentImage
            )

            val dbMessage = MessageController.createMessage(message)
            if (dbMessage == 0L) {
                throw Error("An error has occurred while creating a message.")
            }

            currentMessage = ""
            currentImage = null
            assistantIsTyping = true

            val settings = Settings.values().associateWith { setting ->
                val chatSetting = ChatSettingController.getChatSettingByChatAndId(
                    chatId,
                    SettingController.settingsIds[setting]!!
                ).first()
                (chatSetting?.value ?: setting._default)
            }

            val request = prepareRequest(settings, false)
            sendMessageRequest(request, settings[Settings.ApiEndpoint]!!, false)
        }
    }

    private suspend fun prepareRequest(settings: Map<Settings, String>, continuation: Boolean): Map<String, Any> {
        val rawMessages = _messages.first().toMutableList().apply {
            add(
                0,
                DbMessage(
                    chatId = chatId,
                    role = "system",
                    timestamp = Date().time,
                    content = settings[Settings.Context]!!,
                    image = null
                )
            )
        }

        val preparedMessages = prepareMessages(
            rawMessages,
            settings[Settings.BeginningTemplate]!!,
            settings[Settings.MessageTemplate]!!,
            settings[Settings.UserName]!!,
            settings[Settings.AssistantName]!!,
            settings[Settings.SystemName]!!,
            continuation
        )

        return mapOf(
            "prompt" to preparedMessages.first,
            "image_data" to preparedMessages.second
        ) + settings
            .filter { it.key._category == SettingCategory.Generation }
            .map { it.key._name to deserializeSetting(it.value, it.key._type) }
    }

    private fun sendMessageRequest(request: Map<String, Any>, apiEndpoint: String, continuation: Boolean) {
        val call = ChatApiInstance.getInstance(apiEndpoint).sendMessage(request)
        call.enqueue(object : Callback<SendMessageResponse> {
            override fun onResponse(call: Call<SendMessageResponse>, response: Response<SendMessageResponse>) {
                if(continuation) {
                    viewModelScope.launch {
                        val idToContinue = messages.value.last().id
                        val messageToContinue = MessageController.getMessageById(id = idToContinue).first()!!
                        messageToContinue.content += response.body()?.content ?: ""
                        messageToContinue.timestamp = Date().time
                        MessageController.updateMessage(messageToContinue)

                        assistantIsTyping = false
                    }

                } else {
                    val responseMessage = DbMessage(
                        chatId = chatId,
                        role = "assistant",
                        timestamp = Date().time,
                        content = response.body()?.content?.trim() ?: "",
                        image = null
                    )

                    viewModelScope.launch {
                        val dbMessage = MessageController.createMessage(responseMessage)
                        if (dbMessage == 0L) {
                            throw Error("An error has occurred while creating a message.")
                        }
                        assistantIsTyping = false
                    }
                }
            }

            override fun onFailure(call: Call<SendMessageResponse>, t: Throwable) {
                t.printStackTrace()
                assistantIsTyping = false
            }
        })
    }
}