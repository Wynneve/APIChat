package com.wynneve.apichat.db.controllers

import com.wynneve.apichat.db.GlobalDatabase
import com.wynneve.apichat.db.entities.DbGlobalSetting
import com.wynneve.apichat.db.models.GlobalSettingModel

object GlobalSettingController {
    private val globalSettingModel: GlobalSettingModel by lazy { GlobalDatabase.database.getGlobalSettingDao() }

    suspend fun createGlobalSetting(globalSetting: DbGlobalSetting): Boolean {
        val result = globalSettingModel.createGlobalSetting(globalSetting)
        return (result != 0L)
    }

    suspend fun getGlobalSettingByUserAndId(userId: Int, settingId: Int): DbGlobalSetting {
        return globalSettingModel.getGlobalSettingByUserAndId(userId, settingId)
    }

    suspend fun updateGlobalSetting(globalSetting: DbGlobalSetting): Boolean {
        val result = globalSettingModel.updateGlobalSetting(globalSetting)
        return (result != 0)
    }

    suspend fun deleteGlobalSettingByUserAndId(userId: Int, settingId: Int): Boolean {
        val result = globalSettingModel.deleteGlobalSettingByUserAndId(userId, settingId)
        return (result != 0)
    }
}