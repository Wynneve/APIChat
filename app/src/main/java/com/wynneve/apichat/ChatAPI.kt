package com.wynneve.apichat

import com.wynneve.apichat.db.entities.DbMessage
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
    val messages: Array<JsonMessage>,
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
    val message: JsonMessage,
)

data class JsonMessage(
    @SerializedName("role")
    val role: String,
    @SerializedName("content")
    val content: String,
)

fun MessageToJson(message: DbMessage): JsonMessage {
    return JsonMessage(
        role = message.role,
        content = message.content
    )
}

fun MessagesToJson(messages: List<DbMessage>): Array<JsonMessage> {
    return messages.map{ message -> MessageToJson(message) }.toTypedArray()
}

fun MessageFromJson(JsonMessage: JsonMessage): Message {
    return Message(
        role = JsonMessage.role,
        timestamp = Date().time,
        content = JsonMessage.content
    )
}