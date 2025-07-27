package com.wynneve.apichat.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName="messages", foreignKeys = [
    ForeignKey(
        entity = DbChat::class,
        parentColumns = ["id"],
        childColumns = ["chat_id"],
        onDelete = ForeignKey.CASCADE
    )
])
data class DbMessage(
    @PrimaryKey(autoGenerate=true)
    @ColumnInfo(name="id")
    val id: Int = 0,
    @ColumnInfo(name="chat_id", index=true)
    val chatId: Int,
    @ColumnInfo(name="role")
    val role: String,
    @ColumnInfo(name="timestamp")
    var timestamp: Long,
    @ColumnInfo(name="content")
    var content: String,
    @ColumnInfo(name="image")
    var image: String?
)