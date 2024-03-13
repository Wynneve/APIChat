package com.wynneve.apichat.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(tableName="chat_settings",
    foreignKeys = [
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
    ],
    primaryKeys = [
        "chat_id", "setting_id"
    ]
)
data class DbChatSetting(
    @ColumnInfo(name="chat_id")
    val chatId: Int,
    @ColumnInfo(name="setting_id")
    val settingId: Int,
    @ColumnInfo(name="value")
    val value: String,
)