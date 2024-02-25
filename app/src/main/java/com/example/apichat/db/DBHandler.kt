package com.example.apichat.db

import android.content.Context
import android.database.sqlite.SQLiteOpenHelper

class DBHandler(context: Context?): SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object {
        private const var DB_NAME = ""
    }
}