package com.wynneve.apichat.viewmodels;

import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewModelScope
import com.wynneve.apichat.db.controllers.ProfileController
import com.wynneve.apichat.db.entities.DbProfile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.math.log

class NewProfileViewModel(
    val navigateBack: (callback: () -> Unit) -> Unit,
    val navigateToChats: (id: Int, callback: () -> Unit) -> Unit,
): ViewModel() {
    var login by mutableStateOf("")
    var password by mutableStateOf("")
    var synced by mutableStateOf(true)

    fun loginType(newString: String) {
        login = newString
    }

    fun passwordType(newString: String) {
        password = newString
    }

    fun createProfileClick() {
        synced = false

        viewModelScope.launch(Dispatchers.IO){
            val profile = DbProfile(login = login, password = password)
            val success = async { ProfileController.createProfile(profile) }.await()
            if(!success) throw Error("An error has occurred while creating a profile.")

            val id = async { ProfileController.getProfileByLogin(login).first() }.await()!!.id

            viewModelScope.launch(Dispatchers.Main) {
                synced = true
                navigateToChats(id, {})
            }
        }
    }
}