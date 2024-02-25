package com.example.apichat.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName="global_settings", foreignKeys=[
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
])
data class DbGlobalSetting(
    @PrimaryKey
    @ColumnInfo(name="user_id")
    val userId: Int,
    @PrimaryKey
    @ColumnInfo(name="setting_id")
    val settingId: Int,
    @ColumnInfo(name="value")
    val value: String,
)