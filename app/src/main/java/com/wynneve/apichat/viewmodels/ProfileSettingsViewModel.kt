package com.wynneve.apichat.viewmodels;

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewModelScope
import com.wynneve.apichat.db.Settings
import com.wynneve.apichat.db.controllers.GlobalSettingController
import com.wynneve.apichat.db.controllers.ProfileController
import com.wynneve.apichat.db.controllers.SettingController
import com.wynneve.apichat.db.entities.DbGlobalSetting
import com.wynneve.apichat.db.entities.DbProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Stable
class ProfileSettingsViewModel(
    val loadProfile: (callback: (profile: DbProfile) -> Unit) -> Unit,
    val saveProfile: (profile: DbProfile, callback: () -> Unit) -> Unit,
    val navigateToProfiles: () -> Unit,
    val navigateBack: () -> Unit,
): ViewModel() {
    var profile: DbProfile? = null
    var login by mutableStateOf("")
    var password by mutableStateOf("")

    var synced by mutableStateOf(false)

    var valid by mutableStateOf(true)
    var changed by mutableStateOf(false)

    init {
        loadProfile { profile ->
            this.profile = profile
            this.login = profile.login
            this.password = profile.password
            println("loaded")
        }
    }

    fun onApplySettingsClick(callback: () -> Unit) {
        saveProfile(profile!!, callback)
        changed = false
    }

    fun onLoginType(newText: String) {
        profile!!.login = newText
        login = newText
        changed = true
        if(newText.isBlank()) valid = false
    }

    fun onPasswordType(newText: String) {
        profile!!.password = newText
        password = newText
        changed = true
        if(newText.isBlank()) valid = false
    }
}