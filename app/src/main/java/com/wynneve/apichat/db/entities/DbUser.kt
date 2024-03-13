package com.wynneve.apichat.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="users")
data class DbUser(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    val id: Int = 0,
    @ColumnInfo(name="login", index=true)
    val login: String,
    @ColumnInfo(name="password")
    val password: String,
)