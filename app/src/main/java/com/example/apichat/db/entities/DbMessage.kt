package com.example.apichat.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName="messages", foreignKeys = [
    ForeignKey(
        entity = DbChat::class,
        parentColumns = ["id"],
        childColumns = ["chat_id"]
    )
])
data class DbMessage(
    @PrimaryKey(autoGenerate=true)
    @ColumnInfo(name="id")
    val id: Int,
    @ColumnInfo(name="chat_id")
    val chatId: Int,
    @ColumnInfo(name="role")
    val role: String,
    @ColumnInfo(name="timestamp")
    val timestamp: Int,
    @ColumnInfo(name="content")
    val content: String,
)