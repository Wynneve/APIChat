package com.wynneve.apichat.db.models

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.wynneve.apichat.db.entities.DbGlobalSetting
import kotlinx.coroutines.flow.Flow

@Dao
interface GlobalSettingModel {
    // indexation implies uniqueness
    @Query("SELECT profile_id, setting_id, value FROM global_settings WHERE profile_id = :profileId AND setting_id = :settingId")
    fun getGlobalSettingByProfileAndId(profileId: Int, settingId: Int): Flow<DbGlobalSetting?>

    @Query("SELECT profile_id, setting_id, value FROM global_settings WHERE profile_id = :profileId")
    fun getGlobalSettingsByProfile(profileId: Int): Flow<List<DbGlobalSetting>>

    @Insert(entity = DbGlobalSetting::class)
    suspend fun createGlobalSetting(globalSetting: DbGlobalSetting): Long

    @Update(entity = DbGlobalSetting::class)
    suspend fun updateGlobalSetting(globalSetting: DbGlobalSetting): Int

    @Query("DELETE FROM global_settings WHERE profile_id = :profileId AND setting_id = :settingId")
    suspend fun deleteGlobalSettingByProfileAndId(profileId: Int, settingId: Int): Int

    @Query("DELETE FROM global_settings WHERE profile_id = :profileId")
    suspend fun deleteGlobalSettingsByProfile(profileId: Int): Int
}