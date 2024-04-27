package com.wynneve.apichat.db.models

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.wynneve.apichat.db.entities.DbProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileModel {
    // the login field is indexed, thus there shall be a unique profile
    @Query("SELECT id, login, password FROM profiles WHERE login = :login")
    fun getProfileByLogin(login: String): Flow<DbProfile?>

    @Query("SELECT id, login, password FROM profiles WHERE id = :id")
    fun getProfileById(id: Int): Flow<DbProfile?>

    @Query("SELECT id, login, password FROM profiles")
    fun getProfiles(): Flow<List<DbProfile>>

    @Insert(entity = DbProfile::class)
    suspend fun createProfile(profile: DbProfile): Long

    @Update(entity = DbProfile::class)
    suspend fun updateProfile(profile: DbProfile): Int

    @Query("DELETE FROM profiles WHERE id = :id")
    suspend fun deleteProfileById(id: Int): Int
}