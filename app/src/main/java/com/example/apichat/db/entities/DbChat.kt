package com.example.apichat.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName="chats", foreignKeys = [
    ForeignKey(
        entity = DbUser::class,
        parentColumns = ["id"],
        childColumns = ["user_id"]
    )
])
data class DbChat(
    @PrimaryKey(autoGenerate=true)
    @ColumnInfo(name="id")
    val id: Int,
    @ColumnInfo(name="user_id")
    val userId: Int,
    @ColumnInfo(name="title")
    val title: String,
    @ColumnInfo(name="last_access")
    val lastAccess: Int
)