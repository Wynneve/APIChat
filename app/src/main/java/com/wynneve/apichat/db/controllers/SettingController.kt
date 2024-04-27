package com.wynneve.apichat.db.controllers

import com.wynneve.apichat.db.ApplicationDatabase
import com.wynneve.apichat.db.entities.DbSetting
import com.wynneve.apichat.db.models.SettingModel
import kotlinx.coroutines.flow.Flow

object SettingController {
    private val settingModel: SettingModel by lazy { ApplicationDatabase.database.getSettingDao() }

    suspend fun createSetting(setting: DbSetting): Boolean {
        val result = settingModel.createSetting(setting)
        return (result != 0L)
    }

    fun getSettingById(id: Int): Flow<DbSetting?> {
        return settingModel.getSettingById(id)
    }

    fun getSettingByName(name: String): Flow<DbSetting?> {
        return settingModel.getSettingByName(name)
    }

    suspend fun updateSetting(setting: DbSetting): Boolean {
        val result = settingModel.updateSetting(setting)
        return (result != 0)
    }

    suspend fun deleteSettingById(id: Int): Boolean {
        val result = settingModel.deleteSettingById(id)
        return (result != 0)
    }
}