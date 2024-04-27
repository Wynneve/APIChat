package com.wynneve.apichat.viewmodels;

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewModelScope
import com.wynneve.apichat.db.controllers.ProfileController
import com.wynneve.apichat.db.entities.DbProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ProfilesViewModel(
    val navigateToNewProfile: (callback: () -> Unit) -> Unit,
    val navigateToChats: (id: Int, callback: () -> Unit) -> Unit
): ViewModel() {
    private val _profiles = MutableStateFlow<List<DbProfile>>(emptyList())
    val profiles: StateFlow<List<DbProfile>> = _profiles // ???
    var synced by mutableStateOf(false)

    var selectedProfile by mutableStateOf<DbProfile?>(null)
    var password by mutableStateOf("")

    init {
        viewModelScope.launch(Dispatchers.IO) {
            ProfileController.getProfiles().collect { profiles ->
                viewModelScope.launch(Dispatchers.Main) { synced = false }
                async { _profiles.emit(profiles) }.await()
                viewModelScope.launch(Dispatchers.Main) { synced = true }
            }
        }
    }

    fun loginClick(successCallback: () -> Unit, failureCallback: () -> Unit) {
        if(selectedProfile!!.login == password) {
            navigateToChats(selectedProfile!!.id, successCallback)
        } else {
            failureCallback()
        }
    }
}