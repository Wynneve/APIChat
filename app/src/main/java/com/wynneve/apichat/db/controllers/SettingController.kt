package com.wynneve.apichat.db.controllers

import com.wynneve.apichat.db.GlobalDatabase
import com.wynneve.apichat.db.entities.DbSetting
import com.wynneve.apichat.db.models.SettingModel

object SettingController {
    private val settingModel: SettingModel by lazy { GlobalDatabase.database.getSettingDao() }

    suspend fun createSetting(setting: DbSetting): Boolean {
        val result = settingModel.createSetting(setting)
        return (result != 0L)
    }

    suspend fun getSettingById(id: Int): DbSetting {
        return settingModel.getSettingById(id)
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