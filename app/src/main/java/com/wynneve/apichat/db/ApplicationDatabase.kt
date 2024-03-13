package com.wynneve.apichat.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.wynneve.apichat.db.entities.DbChat
import com.wynneve.apichat.db.entities.DbChatSetting
import com.wynneve.apichat.db.entities.DbGlobalSetting
import com.wynneve.apichat.db.entities.DbMessage
import com.wynneve.apichat.db.entities.DbSetting
import com.wynneve.apichat.db.entities.DbUser
import com.wynneve.apichat.db.models.ChatModel
import com.wynneve.apichat.db.models.ChatSettingModel
import com.wynneve.apichat.db.models.GlobalSettingModel
import com.wynneve.apichat.db.models.MessageModel
import com.wynneve.apichat.db.models.SettingModel
import com.wynneve.apichat.db.models.UserModel

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
abstract class ApplicationDatabase: RoomDatabase() {
    abstract fun getSettingDao(): SettingModel
    abstract fun getUserDao(): UserModel
    abstract fun getGlobalSettingDao(): GlobalSettingModel
    abstract fun getChatDao(): ChatModel
    abstract fun getChatSettingDao(): ChatSettingModel
    abstract fun getMessageDao(): MessageModel
}