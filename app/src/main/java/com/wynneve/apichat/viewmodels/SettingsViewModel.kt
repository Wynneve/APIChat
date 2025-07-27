package com.example.apichat.viewmodels

import android.content.Context
import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wynneve.apichat.ChatApiInstance
import com.wynneve.apichat.SendMessageResponse
import com.wynneve.apichat.db.ApplicationDatabase
import com.wynneve.apichat.db.SettingCategory
import com.wynneve.apichat.db.Settings
import com.wynneve.apichat.db.controllers.ChatSettingController
import com.wynneve.apichat.db.controllers.MessageController
import com.wynneve.apichat.db.controllers.ProfileController
import com.wynneve.apichat.db.controllers.SettingController
import com.wynneve.apichat.db.deserializeSetting
import com.wynneve.apichat.db.entities.DbMessage
import com.wynneve.apichat.db.entities.DbProfile
import com.wynneve.apichat.prepareMessages
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Date

@Stable
class ChatViewModel(
    var chatId: Int,
    val navigateToSettings: () -> Unit
): ViewModel() {
    private val _messages = MutableStateFlow<List<DbMessage>>(emptyList())
    val messages: StateFlow<List<DbMessage>> = _messages
    var synced by mutableStateOf(false)

    lateinit var scrollState: ScrollState

    var currentMessage by mutableStateOf("")
    var currentImage by mutableStateOf("")
    var imageAttached by mutableStateOf(false)
    var assistantIsTyping by mutableStateOf(false)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            MessageController.getMessagesByChat(chatId).collect { messages ->
                viewModelScope.launch(Dispatchers.Main) { synced = false }
                async { _messages.emit(messages) }.await()
                viewModelScope.launch(Dispatchers.Main) { synced = true }
            }
        }
    }

    // Controllers
    fun onMessageType(newText: String) {
        currentMessage = newText
    }

    fun onSendClick() {
        if(this.currentMessage.isBlank()) return

        viewModelScope.launch(Dispatchers.IO) {
            val message = DbMessage(
                chatId = chatId,
                role = "user",
                timestamp = Date().time,
                content = currentMessage,
                image = if(imageAttached) currentImage else null
            )

            val dbMessage = async { MessageController.createMessage(message) }.await()
            if(dbMessage == 0L) throw Error("An error has occurred while creating a message.")

            viewModelScope.launch(Dispatchers.Main) {
                currentMessage = ""
                scrollState.scrollTo(Int.MAX_VALUE)
                assistantIsTyping = true
            }

            val settings = Settings.values().associateWith { s ->
                ChatSettingController.getChatSettingByChatAndId(
                    chatId,
                    SettingController.settingsIds[s]!!
                ).first()!!.value
            }.toMutableMap()

            val request = settings.filter {
                p -> p.key._category == SettingCategory.Generation
            }.map { p ->
                Pair(p.key._name, deserializeSetting(p.value, p.key._type))
            }.toMap().toMutableMap()

            val raw = messages.first().toMutableList()
            raw.add(0, DbMessage(
                chatId=chatId,
                role="system",
                timestamp=Date().time,
                content=settings[Settings.Context]!!,
                image=null
            ))

            val prepared = prepareMessages(
                raw,
                settings[Settings.BeginningTemplate]!!,
                settings[Settings.MessageTemplate]!!,
                settings[Settings.UserName]!!,
                settings[Settings.AssistantName]!!,
                settings[Settings.SystemName]!!,
            )

            request["prompt"] = prepared.first
            request["image_data"] = prepared.second

            val call = ChatApiInstance.getInstance(settings[Settings.ApiEndpoint]!!).sendMessage(request)
            call.enqueue(object : Callback<SendMessageResponse> {
                override fun onResponse(
                    call: Call<SendMessageResponse>,
                    response: Response<SendMessageResponse>
                ) {
                    val responseMessage = DbMessage(
                        chatId = chatId,
                        role = "assistant",
                        timestamp = Date().time,
                        content = response.body()!!.content.trim(),
                        image = null
                    )
                    viewModelScope.launch(Dispatchers.IO) {
                        val dbMessage = async { MessageController.createMessage(responseMessage) }.await()
                        if(dbMessage == 0L) throw Error("An error has occurred while creating a message.")

                        viewModelScope.launch(Dispatchers.Main) {
                            scrollState.animateScrollTo(Int.MAX_VALUE)
                            assistantIsTyping = false
                        }
                    }
                }

                override fun onFailure(
                    call: Call<SendMessageResponse>,
                    t: Throwable
                ) {
                    println(t.message)
                    throw t
                }
            })
        }
    }
}