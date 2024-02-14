package com.example.apichat

import android.content.Context
import android.database.sqlite.SQLiteOpenHelper

data class MessageDAO(
    val id: Int,
    val chat_id: Int,
    val role: String,
    val content: String,
)

data class SettingsDAO(
    
)

class DBHandler(context: Context?): SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object {
        private const var DB_NAME = ""
    }
}