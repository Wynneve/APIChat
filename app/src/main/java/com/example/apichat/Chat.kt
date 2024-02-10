package com.example.apichat

import android.content.Context
import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Date

@Stable
data class Chat(
    val messages: MutableList<Message> = mutableStateListOf(),
    val assistantIsTyping: MutableState<Boolean> = mutableStateOf(false),
    val currentMessage: MutableState<String> = mutableStateOf(""),
)

@Stable
class ChatController(val chat: Chat, val settings: SettingsViewModel, val context: Context, val scope: CoroutineScope, val navigateToSettings: () -> Unit) {
    // Parameters
    var scrollState: ScrollState? = null

    // Controllers
    fun onMessageType(newText: String) {
        chat.currentMessage.value = newText
    }

    fun onSendClick() {
        if(chat.currentMessage.value.isBlank()) return

        chat.messages.add(Message(role=Role.User, date=Date(), content=chat.currentMessage.value))
        chat.currentMessage.value = ""
        scope.launch {
            scrollState!!.animateScrollTo(Int.MAX_VALUE)
        }

        chat.assistantIsTyping.value = true
        sendMessage(chat.messages) {
            chat.messages.add(it)
            scope.launch {
                scrollState!!.animateScrollTo(Int.MAX_VALUE)
            }

            chat.assistantIsTyping.value = false
        }
    }

    fun onSettingsClick() {
        navigateToSettings()
    }

    fun sendMessage(messages: List<Message>, responseCallback: (Message) -> Unit) {
        val messagesJSON: Array<MessageJSON> = MessagesToJSON(messages)
        val sendMessageRequest = SendMessageRequest(
            mode = "chat-instruct",

            messages = messagesJSON,
            max_tokens = settings.get(Setting.maxTokens).toInt(),
            repetition_penalty = settings.get(Setting.repetitionPenalty).toFloat(),
            temperature = settings.get(Setting.temperature).toFloat(),
            top_p = settings.get(Setting.topP).toFloat(),

            user_name = settings.get(Setting.userName),
            bot_name = settings.get(Setting.botName),
            context = settings.get(Setting.context),
        )
        val call: Call<SendMessageResponse> =
            ChatAPIInstance.getInstance(settings.get(Setting.apiEndpoint)).sendMessage(sendMessageRequest)

        call.enqueue(object : Callback<SendMessageResponse> {
            override fun onResponse(
                call: Call<SendMessageResponse>,
                response: Response<SendMessageResponse>
            ) {
                println(response)
                responseCallback(MessageFromJSON(response.body()!!.choices[0].message))
            }

            override fun onFailure(
                call: Call<SendMessageResponse>,
                t: Throwable) {
                println(t)
            }
        })
    }
}