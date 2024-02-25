package com.example.apichat.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="settings")
data class DbSetting(
    @PrimaryKey(autoGenerate=true)
    @ColumnInfo(name="id")
    val id: Int,
    @ColumnInfo(name="name", index=true)
    val name: String,
    @ColumnInfo(name="default")
    val default: String,
)