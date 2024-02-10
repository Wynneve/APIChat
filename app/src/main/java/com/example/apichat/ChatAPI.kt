package com.example.apichat

import androidx.compose.runtime.*
import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.Date
import java.util.concurrent.TimeUnit

interface ChatAPI {
    @POST("/v1/chat/completions")
    fun sendMessage(@Body sendMessageModel: SendMessageRequest): Call<SendMessageResponse>
}

object ChatAPIInstance {
    private var instance: ChatAPI? = null
    private var client: OkHttpClient = OkHttpClient.Builder()
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()
    private var lastEndpoint: String = ""

    fun getInstance(apiEndpointURL: String): ChatAPI {
        if(instance == null || lastEndpoint != apiEndpointURL) {
            instance = Retrofit.Builder()
                .baseUrl(apiEndpointURL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(ChatAPI::class.java)
        }
        lastEndpoint = apiEndpointURL
        return instance!!
    }
}

data class SendMessageRequest(
    @SerializedName("mode")
    val mode: String,

    @SerializedName("messages")
    val messages: Array<MessageJSON>,
    @SerializedName("max_tokens")
    val max_tokens: Int,
    @SerializedName("repetition_penalty")
    val repetition_penalty: Float,
    @SerializedName("temperature")
    val temperature: Float,
    @SerializedName("top_p")
    val top_p: Float,

    @SerializedName("name1")
    val user_name: String,
    @SerializedName("name2")
    val bot_name: String,
    @SerializedName("context")
    val context: String
)

data class SendMessageResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("object")
    val `object`: String,
    @SerializedName("created")
    val created: String,
    @SerializedName("model")
    val model: String,
    @SerializedName("choices")
    val choices: Array<MessageChoiceResponse>,
)

data class MessageChoiceResponse(
    @SerializedName("index")
    val index: Int,
    @SerializedName("finish_reason")
    val finish_reason: String,
    @SerializedName("message")
    val message: MessageJSON,
)

data class MessageJSON(
    @SerializedName("role")
    val role: String,
    @SerializedName("content")
    val content: String,
)

fun MessageToJSON(message: Message): MessageJSON {
    return MessageJSON(
        role = roleToString(message.role),
        content = message.content
    )
}

fun MessagesToJSON(messages: List<Message>): Array<MessageJSON> {
    return messages.map{ message -> MessageToJSON(message) }.toTypedArray()
}

fun MessageFromJSON(messageJSON: MessageJSON): Message {
    return Message(
        role = stringToRole(messageJSON.role),
        date = Date(),
        content = messageJSON.content
    )
}