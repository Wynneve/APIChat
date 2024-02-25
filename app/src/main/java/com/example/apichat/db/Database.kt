package com.example.apichat.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.apichat.db.entities.DbChat
import com.example.apichat.db.entities.DbChatSetting
import com.example.apichat.db.entities.DbGlobalSetting
import com.example.apichat.db.entities.DbMessage
import com.example.apichat.db.entities.DbSetting
import com.example.apichat.db.entities.DbUser
import com.example.apichat.db.models.ChatModel
import com.example.apichat.db.models.ChatSettingModel
import com.example.apichat.db.models.GlobalSettingModel
import com.example.apichat.db.models.MessageModel
import com.example.apichat.db.models.SettingModel
import com.example.apichat.db.models.UserModel

@Database(
    version = 1,
    entities = [
        DbSetting::class,
        DbUser::class,
        DbGlobalSetting::class,
        DbChat::class,
        DbChatSetting::class,
        DbMessage::class,
    ]
)
abstract class Database: RoomDatabase() {
    abstract fun getSettingDao(): SettingModel
    abstract fun getUserDao(): UserModel
    abstract fun getGlobalSettingDao(): GlobalSettingModel
    abstract fun getChatDao(): ChatModel
    abstract fun getChatSettingDao(): ChatSettingModel
    abstract fun getMessageDao(): MessageModel
}