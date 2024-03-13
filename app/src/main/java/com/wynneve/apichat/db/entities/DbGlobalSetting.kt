package com.wynneve.apichat.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(tableName="global_settings",
    foreignKeys=[
        ForeignKey(
            entity = DbUser::class,
            parentColumns = ["id"],
            childColumns = ["user_id"]
        ),
        ForeignKey(
            entity = DbSetting::class,
            parentColumns = ["id"],
            childColumns = ["setting_id"]
        ),
    ],
    primaryKeys = [
        "user_id", "setting_id"
    ]
)
data class DbGlobalSetting(
    @ColumnInfo(name="user_id")
    val userId: Int,
    @ColumnInfo(name="setting_id")
    val settingId: Int,
    @ColumnInfo(name="value")
    val value: String,
)