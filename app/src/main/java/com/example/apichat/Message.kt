package com.example.apichat

import androidx.compose.runtime.Stable
import java.util.Date

enum class Role {
    User,
    Assistant(),
}

fun roleToString(role: Role): String {
    return when(role) {
        Role.User -> "user"
        Role.Assistant -> "assistant"
    }
}

fun stringToRole(role: String): Role {
    return when(role) {
        "user" -> Role.User
        "assistant" -> Role.Assistant
        else -> throw Error("Can't cast string `${role}` to Role.")
    }
}

@Stable
data class Message(val role: Role, val date: Date, val content: String)