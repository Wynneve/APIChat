package com.example.apichat.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName="chat_settings", foreignKeys = [
    ForeignKey(
        entity = DbChat::class,
        parentColumns = ["id"],
        childColumns = ["chat_id"]
    ),
    ForeignKey(
        entity = DbSetting::class,
        parentColumns = ["id"],
        childColumns = ["setting_id"]
    )
])
data class DbChatSetting(
    @PrimaryKey
    @ColumnInfo(name="chat_id")
    val chatId: Int,
    @PrimaryKey
    @ColumnInfo(name="setting_id")
    val settingId: Int,
    @ColumnInfo(name="value")
    val value: String,
)