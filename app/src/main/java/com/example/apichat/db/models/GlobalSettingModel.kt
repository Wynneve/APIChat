package com.example.apichat.db.models

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.apichat.db.entities.DbGlobalSetting

@Dao
interface GlobalSettingModel {
    // indexation implies uniqueness
    @Query("SELECT user_id, setting_id, value FROM global_settings WHERE user_id = :userId AND setting_id = :settingId")
    suspend fun getGlobalSettingByUserAndId(userId: Int, settingId: Int): DbGlobalSetting

    @Query("SELECT user_id, setting_id, value FROM global_settings WHERE user_id = :userId")
    suspend fun getGlobalSettingsByUser(userId: Int): Array<DbGlobalSetting>

    @Insert(entity = DbGlobalSetting::class)
    suspend fun createGlobalSetting(globalSetting: DbGlobalSetting): Long

    @Update(entity = DbGlobalSetting::class)
    suspend fun updateGlobalSetting(globalSetting: DbGlobalSetting): Int

    @Query("DELETE FROM global_settings WHERE user_id = :userId AND setting_id = :settingId")
    suspend fun deleteGlobalSettingByUserAndId(userId: Int, settingId: Int): Int

    @Query("DELETE FROM global_settings WHERE user_id = :userId")
    suspend fun deleteGlobalSettingsByUser(userId: Int): Int
}