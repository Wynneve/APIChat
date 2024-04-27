package com.wynneve.apichat.db

import android.annotation.SuppressLint
import android.content.Context
import androidx.room.Room

@SuppressLint("StaticFieldLeak")
object ApplicationDatabase {
    private lateinit var context: Context

    public val database: DatabaseInterface by lazy {
        Room.databaseBuilder(this.context, DatabaseInterface::class.java, "db.db")

            .build()
    }

    fun initialize(context: Context) {
        this.context = context
    }
}