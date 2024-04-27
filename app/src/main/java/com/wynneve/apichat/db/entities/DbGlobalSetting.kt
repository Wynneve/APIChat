package com.wynneve.apichat.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(tableName="global_settings",
    foreignKeys=[
        ForeignKey(
            entity = DbProfile::class,
            parentColumns = ["id"],
            childColumns = ["profile_id"]
        ),
        ForeignKey(
            entity = DbSetting::class,
            parentColumns = ["id"],
            childColumns = ["setting_id"]
        ),
    ],
    primaryKeys = [
        "profile_id", "setting_id"
    ]
)
data class DbGlobalSetting(
    @ColumnInfo(name="profile_id")
    val profileId: Int,
    @ColumnInfo(name="setting_id")
    val settingId: Int,
    @ColumnInfo(name="value")
    val value: String,
)