package com.example.apichat

import java.util.Date

enum class Role(s: String) {
    User("user"),
    Assistant("assistant"),
}

data class Message(val author: String, val date: Date, val content: String)