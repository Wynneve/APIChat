package com.wynneve.apichat.db.controllers

import com.wynneve.apichat.db.ApplicationDatabase
import com.wynneve.apichat.db.entities.DbGlobalSetting
import com.wynneve.apichat.db.models.GlobalSettingModel
import kotlinx.coroutines.flow.Flow

object GlobalSettingController {
    private val globalSettingModel: GlobalSettingModel by lazy { ApplicationDatabase.database.getGlobalSettingDao() }

    suspend fun createGlobalSetting(globalSetting: DbGlobalSetting): Boolean {
        val result = globalSettingModel.createGlobalSetting(globalSetting)
        return (result != 0L)
    }

    fun getGlobalSettingByUserAndId(profileId: Int, settingId: Int): Flow<DbGlobalSetting?> {
        return globalSettingModel.getGlobalSettingByProfileAndId(profileId, settingId)
    }

    suspend fun updateGlobalSetting(globalSetting: DbGlobalSetting): Boolean {
        val result = globalSettingModel.updateGlobalSetting(globalSetting)
        return (result != 0)
    }

    suspend fun deleteGlobalSettingByUserAndId(profileId: Int, settingId: Int): Boolean {
        val result = globalSettingModel.deleteGlobalSettingByProfileAndId(profileId, settingId)
        return (result != 0)
    }
}