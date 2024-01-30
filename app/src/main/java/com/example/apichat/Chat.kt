package com.example.apichat

import android.content.Context
import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Date

class Chat(val settings: Settings, val context: Context, val scope: CoroutineScope, val navigateToSettings: () -> Unit) {
    // Parameters
    var scrollState: ScrollState? = null

    // State
    val messages: MutableList<Message> = mutableStateListOf()
    val botIsTyping: MutableState<Boolean> = mutableStateOf(false)
    val currentMessage: MutableState<String> = mutableStateOf("")

    // Controllers
    fun onMessageType(newText: String) {
        currentMessage.value = newText
    }

    fun onSendClick() {
        if(currentMessage.value.isBlank()) return

        messages.add(Message(author="user", date=Date(), content=currentMessage.value))
        currentMessage.value = ""
        scope.launch {
            scrollState!!.animateScrollTo(Int.MAX_VALUE)
        }

        botIsTyping.value = true
        sendMessage(messages) {
            messages.add(it)
            scope.launch {
                scrollState!!.animateScrollTo(Int.MAX_VALUE)
            }

            botIsTyping.value = false
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
            max_tokens = settings.values[Setting.maxTokens]!!.toInt(),
            repetition_penalty = settings.values[Setting.repetitionPenalty]!!.toFloat(),
            temperature = settings.values[Setting.temperature]!!.toFloat(),
            top_p = settings.values[Setting.topP]!!.toFloat(),

            user_name = settings.values[Setting.userName]!!,
            bot_name = settings.values[Setting.botName]!!,
            context = settings.values[Setting.context]!!,
        )
        val call: Call<SendMessageResponse> =
            ChatAPIInstance.getInstance(settings.values[Setting.apiEndpoint]!!).sendMessage(sendMessageRequest)

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