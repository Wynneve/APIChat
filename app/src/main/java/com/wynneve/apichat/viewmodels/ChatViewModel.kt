//package com.example.apichat.viewmodels
//
//import android.content.Context
//import androidx.compose.foundation.ScrollState
//import androidx.compose.runtime.MutableState
//import androidx.compose.runtime.Stable
//import androidx.compose.runtime.mutableStateListOf
//import androidx.compose.runtime.mutableStateOf
//import com.example.apichat.ChatAPIInstance
//import com.example.apichat.JsonMessage
//import com.example.apichat.Message
//import com.example.apichat.MessageFromJson
//import com.example.apichat.MessagesToJson
//import com.example.apichat.SendMessageRequest
//import com.example.apichat.SendMessageResponse
//import com.example.apichat.Setting
//import com.example.apichat.SettingsViewModel
//import com.example.apichat.db.entities.DbMessage
//import com.example.apichat.roleUser
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.launch
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import java.util.Date
//
//@Stable
//data class Chat(
//    val messages: MutableList<DbMessage> = mutableStateListOf(),
//    val assistantIsTyping: MutableState<Boolean> = mutableStateOf(false),
//    val currentMessage: MutableState<String> = mutableStateOf(""),
//)
//
//@Stable
//class ChatController(val chat: Chat, val settings: SettingsViewModel, val context: Context, val scope: CoroutineScope, val navigateToSettings: () -> Unit) {
//    // Parameters
//    var scrollState: ScrollState? = null
//
//    // Controllers
//    fun onMessageType(newText: String) {
//        chat.currentMessage.value = newText
//    }
//
//    fun onSendClick() {
//        if(chat.currentMessage.value.isBlank()) return
//
//        chat.messages.add(Message(role = roleUser, timestamp=Date().time, content =chat.currentMessage.value))
//        chat.currentMessage.value = ""
//        scope.launch {
//            scrollState!!.animateScrollTo(Int.MAX_VALUE)
//        }
//
//        chat.assistantIsTyping.value = true
//        sendMessage(chat.messages) {
//            chat.messages.add(it)
//            scope.launch {
//                scrollState!!.animateScrollTo(Int.MAX_VALUE)
//            }
//
//            chat.assistantIsTyping.value = false
//        }
//    }
//
//    fun onSettingsClick() {
//        navigateToSettings()
//    }
//
//    fun sendMessage(messages: List<DbMessage>, responseCallback: (Message) -> Unit) {
//        val messagesJSON: Array<JsonMessage> = MessagesToJson(messages)
//        val sendMessageRequest = SendMessageRequest(
//            mode = "chat-instruct",
//
//            messages = messagesJSON,
//            max_tokens = settings.get(Setting.maxTokens).toInt(),
//            repetition_penalty = settings.get(Setting.repetitionPenalty).toFloat(),
//            temperature = settings.get(Setting.temperature).toFloat(),
//            top_p = settings.get(Setting.topP).toFloat(),
//
//            user_name = settings.get(Setting.userName),
//            bot_name = settings.get(Setting.botName),
//            context = settings.get(Setting.context),
//        )
//        val call: Call<SendMessageResponse> =
//            ChatAPIInstance.getInstance(settings.get(Setting.apiEndpoint)).sendMessage(sendMessageRequest)
//
//        call.enqueue(object : Callback<SendMessageResponse> {
//            override fun onResponse(
//                call: Call<SendMessageResponse>,
//                response: Response<SendMessageResponse>
//            ) {
//                println(response)
//                responseCallback(MessageFromJson(response.body()!!.choices[0].message))
//            }
//
//            override fun onFailure(
//                call: Call<SendMessageResponse>,
//                t: Throwable) {
//                println(t)
//            }
//        })
//    }
//}