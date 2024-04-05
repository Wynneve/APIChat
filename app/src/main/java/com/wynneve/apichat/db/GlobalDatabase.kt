package com.wynneve.apichat.db

import android.annotation.SuppressLint
import android.content.Context
import androidx.room.Room

@SuppressLint("StaticFieldLeak")
object GlobalDatabase {
    private lateinit var context: Context

    public val database: DatabaseInstance by lazy {
        Room.databaseBuilder(this.context, DatabaseInstance::class.java, "db.db")
            .createFromAsset("db.db")
            .build()
    }

    fun init(context: Context) {
        this.context = context
    }
}