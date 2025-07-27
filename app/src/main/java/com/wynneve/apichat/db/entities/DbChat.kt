package com.wynneve.apichat.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName="chats", foreignKeys = [
    ForeignKey(
        entity = DbProfile::class,
        parentColumns = ["id"],
        childColumns = ["profile_id"],
        onDelete = ForeignKey.CASCADE
    )
])
data class DbChat(
    @PrimaryKey(autoGenerate=true)
    @ColumnInfo(name="id")
    val id: Int = 0,
    @ColumnInfo(name="profile_id", index=true)
    val profileId: Int,
    @ColumnInfo(name="title")
    var title: String,
    @ColumnInfo(name="last_access")
    val lastAccess: Long
)