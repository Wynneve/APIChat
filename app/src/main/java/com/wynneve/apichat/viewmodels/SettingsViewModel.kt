package com.wynneve.apichat.viewmodels

import android.content.Context
import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wynneve.apichat.ChatApiInstance
import com.wynneve.apichat.SendMessageResponse
import com.wynneve.apichat.db.ApplicationDatabase
import com.wynneve.apichat.db.SettingCategory
import com.wynneve.apichat.db.Settings
import com.wynneve.apichat.db.controllers.ChatSettingController
import com.wynneve.apichat.db.controllers.MessageController
import com.wynneve.apichat.db.controllers.ProfileController
import com.wynneve.apichat.db.controllers.SettingController
import com.wynneve.apichat.db.deserializeSetting
import com.wynneve.apichat.db.entities.DbMessage
import com.wynneve.apichat.db.entities.DbProfile
import com.wynneve.apichat.db.validateSetting
import com.wynneve.apichat.prepareMessages
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Date

@Stable
class SettingsViewModel(
    val loadSettings: (callback: (settings: SnapshotStateMap<Settings, String>) -> Unit) -> Unit,
    val saveSettings: (settings: SnapshotStateMap<Settings, String>, callback: () -> Unit) -> Unit,
    val navigateBack: () -> Unit
): ViewModel() {
    var synced by mutableStateOf(false)
    var valid by mutableStateOf(true)
    var changed by mutableStateOf(false)

    var settings by mutableStateOf<SnapshotStateMap<Settings, String>?>(null)

    init {
        loadSettings { settings ->
            this.settings = settings
            println("loaded")
        }
    }

    fun onSettingType(setting: Settings, newText: String) {
        settings?.set(setting, newText)
        changed = true
        valid = Settings.values().all { setting ->
            validateSetting(settings?.get(setting)!!, setting._type)
        }
    }

    fun onSaveClick(callback: () -> Unit) {
        saveSettings(settings!!, callback)
        changed = false
    }
}