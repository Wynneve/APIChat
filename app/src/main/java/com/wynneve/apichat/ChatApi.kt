package com.wynneve.apichat

import com.google.gson.annotations.SerializedName
import com.wynneve.apichat.db.entities.DbMessage
import kotlinx.coroutines.CancellableContinuation
import okhttp3.OkHttpClient
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface ChatApi {
    @JvmSuppressWildcards
    @POST("/completion")
    fun sendMessage(@Body sendMessageRequest: Map<String, Any>): Call<SendMessageResponse>
}

object ChatApiInstance {
    private var instance: ChatApi? = null
    private var client: OkHttpClient = OkHttpClient.Builder()
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .connectTimeout(60, TimeUnit.SECONDS)
        .build()
    private var lastEndpoint: String = ""

    fun getInstance(apiEndpointURL: String): ChatApi {
        if(instance == null || lastEndpoint != apiEndpointURL) {
            instance = Retrofit.Builder()
                .baseUrl(apiEndpointURL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(ChatApi::class.java)
            lastEndpoint = apiEndpointURL
        }

        return instance!!
    }
}

data class SendMessageResponse(
    @SerializedName("content")
    val content: String,
    @SerializedName("stop")
    val stop: Boolean,
    @SerializedName("stopped_eos")
    val stopped_eos: Boolean,
    @SerializedName("stopped_limit")
    val stopped_limit: Boolean,
    @SerializedName("stopped_word")
    val stopped_word: Boolean,
)

fun prepareMessages(
    messages: List<DbMessage>,
    beginningTemplate: String,
    messageTemplate: String,
    userName: String,
    assistantName: String,
    systemName: String,
    continuation: Boolean // Guaranteed to have a message by the assistant as the last
): Pair<String, List<Map<String, Any>>> {
    val imageData = messages.filter { m ->
        m.image != null
    }.map { m ->
        mapOf<String, Any>(Pair("id", m.id), Pair("data", m.image!!))
    }

    var preparedMessages = messages.joinToString("") { m ->
        messageTemplate
            .replace(
                "{role}", when (m.role) {
                    "user" -> userName
                    "assistant" -> assistantName
                    "system" -> systemName
                    else -> "unknown"
                }
            )
            .replace("{content}", (if (m.image != null) "[img-${m.id}] " else "") + m.content)
    }

    val assistantSuffix = messageTemplate.replace("{role}", assistantName).substringBefore("{content}").trim(' ')

    if(continuation) {
        preparedMessages = preparedMessages.removeSuffix(messageTemplate.substringAfter("{content}"))
        return Pair(beginningTemplate + preparedMessages, imageData)
    } else {
        return Pair(beginningTemplate + preparedMessages + assistantSuffix, imageData)
    }
}