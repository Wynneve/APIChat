package com.wynneve.apichat.db

import android.annotation.SuppressLint
import android.content.Context
import androidx.room.Room
import com.wynneve.apichat.db.controllers.SettingController
import com.wynneve.apichat.db.entities.DbSetting
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.newCoroutineContext
import kotlinx.coroutines.runBlocking

@SuppressLint("StaticFieldLeak")
object ApplicationDatabase {
    private lateinit var context: Context

    public val database: DatabaseInterface by lazy {
        Room.databaseBuilder(this.context, DatabaseInterface::class.java, "data.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    fun initialize(context: Context) {
        this.context = context

        runBlocking(Dispatchers.IO) {
            for(defaultSetting in Settings.values()) {
                var setting = async { SettingController.getSettingByName(defaultSetting._name).first() }.await()?.id
                if(setting == null) {
                    val dbSetting = async { SettingController.createSetting(DbSetting(
                        name = defaultSetting._name,
                        default = defaultSetting._default
                    )) }.await()
                    if(dbSetting == 0L) throw Error("An error has occurred while creating a setting.")

                    setting = dbSetting.toInt()
                }

                SettingController.settingsIds[defaultSetting] = setting
            }
        }
    }
}