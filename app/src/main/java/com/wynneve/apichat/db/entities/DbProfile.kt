package com.wynneve.apichat.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName="profiles",
    indices = [
        Index(
            value = [ "login" ],
            unique = true
        )
    ]
)
data class DbProfile(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    val id: Int = 0,
    @ColumnInfo(name="login")
    var login: String,
    @ColumnInfo(name="password")
    var password: String,
)