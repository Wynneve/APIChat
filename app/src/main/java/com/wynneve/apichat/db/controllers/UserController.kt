package com.wynneve.apichat.db.controllers

import com.wynneve.apichat.db.GlobalDatabase
import com.wynneve.apichat.db.entities.DbUser
import com.wynneve.apichat.db.models.UserModel

object UserController {
    private val userModel: UserModel by lazy { GlobalDatabase.database.getUserDao() }

    suspend fun createUser(user: DbUser): Boolean {
        val result = userModel.createUser(user)
        return (result != 0L)
    }

    suspend fun getUserById(id: Int): DbUser {
        return userModel.getUserById(id)
    }

    suspend fun getUserByLogin(login: String): DbUser {
        return userModel.getUserByLogin(login)
    }

    suspend fun updateUser(user: DbUser): Boolean {
        val result = userModel.updateUser(user)
        return (result != 0)
    }

    suspend fun deleteUserById(id: Int): Boolean {
        val result = userModel.deleteUserById(id)
        return (result != 0)
    }
}