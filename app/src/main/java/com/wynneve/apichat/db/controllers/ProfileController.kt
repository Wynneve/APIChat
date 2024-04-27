package com.wynneve.apichat.db.controllers

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.LiveData
import com.wynneve.apichat.db.ApplicationDatabase
import com.wynneve.apichat.db.entities.DbProfile
import com.wynneve.apichat.db.models.ProfileModel
import kotlinx.coroutines.flow.Flow

object ProfileController {
    private val profileModel: ProfileModel by lazy { ApplicationDatabase.database.getProfileDao() }

    suspend fun createProfile(profile: DbProfile): Boolean {
        val result = profileModel.createProfile(profile)
        return (result != 0L)
    }

    fun getProfileById(id: Int): Flow<DbProfile?> {
        return profileModel.getProfileById(id)
    }

    fun getProfileByLogin(login: String): Flow<DbProfile?> {
        return profileModel.getProfileByLogin(login)
    }

    fun getProfiles(): Flow<List<DbProfile>> {
        return profileModel.getProfiles()
    }

    suspend fun updateProfile(profile: DbProfile): Boolean {
        val result = profileModel.updateProfile(profile)
        return (result != 0)
    }

    suspend fun deleteProfileById(id: Int): Boolean {
        val result = profileModel.deleteProfileById(id)
        return (result != 0)
    }
}