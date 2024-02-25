package com.example.apichat.db.models

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.apichat.db.entities.DbSetting

@Dao
interface SettingModel {
    // indexation implies uniqueness
    @Query("SELECT id, name, `default` FROM settings WHERE name = :name")
    suspend fun getSettingByName(name: String): DbSetting

    @Query("SELECT id, name, `default` FROM settings WHERE id = :id")
    suspend fun getSettingById(id: Int): DbSetting

    @Insert(entity = DbSetting::class)
    suspend fun createSetting(setting: DbSetting): Long

    @Update(entity = DbSetting::class)
    suspend fun updateSetting(setting: DbSetting): Int

    @Query("DELETE FROM settings WHERE id = :id")
    suspend fun deleteSettingById(id: Int): Int
}