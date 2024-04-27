package com.wynneve.apichat.db.models

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.wynneve.apichat.db.entities.DbSetting
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingModel {
    // indexation implies uniqueness
    @Query("SELECT id, name, `default` FROM settings WHERE name = :name")
    fun getSettingByName(name: String): Flow<DbSetting?>

    @Query("SELECT id, name, `default` FROM settings WHERE id = :id")
    fun getSettingById(id: Int): Flow<DbSetting?>

    @Insert(entity = DbSetting::class)
    suspend fun createSetting(setting: DbSetting): Long

    @Update(entity = DbSetting::class)
    suspend fun updateSetting(setting: DbSetting): Int

    @Query("DELETE FROM settings WHERE id = :id")
    suspend fun deleteSettingById(id: Int): Int
}