package com.example.apichat.db.models

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.apichat.db.entities.DbUser

@Dao
interface UserModel {
    // the login field is indexed, thus there shall be a unique user
    @Query("SELECT id, login, password FROM users WHERE login = :login")
    suspend fun getUserByLogin(login: String): DbUser

    @Query("SELECT id, login, password FROM users WHERE id = :id")
    suspend fun getUserById(id: Int): DbUser

    @Insert(entity = DbUser::class)
    suspend fun createUser(user: DbUser): Long

    @Update(entity = DbUser::class)
    suspend fun updateUser(user: DbUser): Int

    @Query("DELETE FROM users WHERE id = :id")
    suspend fun deleteUserById(id: Int): Int
}