package com.example.apichat

import androidx.compose.runtime.Stable

@Stable
data class Message(val role: String, val timestamp: Long, val content: String)