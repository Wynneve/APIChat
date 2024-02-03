package com.example.apichat

import android.content.Context
import android.webkit.URLUtil
import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

enum class Setting {
    apiEndpoint,
    maxTokens,
    repetitionPenalty,
    temperature,
    topP,
    userName,
    botName,
    context,
}

@Stable
data class Settings(
    @Stable
    val defaults: Map<Setting, String> = mapOf<Setting, String>(
        Pair(Setting.apiEndpoint, "0.0.0.0"),
        Pair(Setting.maxTokens, 512.toString()),
        Pair(Setting.repetitionPenalty, 1.15f.toString()),
        Pair(Setting.temperature, 0.7f.toString()),
        Pair(Setting.topP, 0.9f.toString()),

        Pair(Setting.userName, "User"),
        Pair(Setting.botName, "Bot"),
        Pair(Setting.context, "This is a conversation between the User and LLM-powered AI Assistant named Bot."),
    ),
    @Stable
    val keys: Map<Setting, Preferences.Key<*>> = mapOf<Setting, Preferences.Key<*>>(
        Pair(Setting.apiEndpoint, stringPreferencesKey("apiEndpoint")),
        Pair(Setting.maxTokens, intPreferencesKey("maxTokens")),
        Pair(Setting.repetitionPenalty, floatPreferencesKey("repetitionPenalty")),
        Pair(Setting.temperature, floatPreferencesKey("temperature")),
        Pair(Setting.topP, floatPreferencesKey("topP")),

        Pair(Setting.userName, stringPreferencesKey("userName")),
        Pair(Setting.botName, stringPreferencesKey("botName")),
        Pair(Setting.context, stringPreferencesKey("context"))
    ),

    @Stable
    val values: SnapshotStateMap<Setting, String> = mutableStateMapOf<Setting, String>(),
    @Stable
    val validations: MutableMap<Setting, Boolean> = mutableMapOf<Setting, Boolean>(),
    @Stable
    val valid: MutableState<Boolean> = mutableStateOf(true),

    @Stable
    val changed: MutableState<Boolean> = mutableStateOf(false)
): ViewModel()

class SettingsController(val settings: Settings, val context: Context, val scope: CoroutineScope, val navigateBack: () -> Unit) {
    companion object {
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    }

    init {
        loadSettings()
    }

    var scrollState: ScrollState? = null

    fun onApiEndpointType(newText: String) {
        settings.changed.value = true

        settings.validations[Setting.apiEndpoint] = URLUtil.isValidUrl(newText)
        validate()

        settings.values[Setting.apiEndpoint] = newText
    }

    fun onMaxTokensType(newText: String) {
        settings.changed.value = true

        val convertedMaxTokens = newText.toIntOrNull()
        settings.validations[Setting.maxTokens] = convertedMaxTokens != null
        validate()

        settings.values[Setting.maxTokens] = newText
    }

    fun onRepetitionPenaltyType(newText: String) {
        settings.changed.value = true

        val convertedRepetitionPenalty = newText.toFloatOrNull()
        settings.validations[Setting.repetitionPenalty] = convertedRepetitionPenalty != null
        validate()

        settings.values[Setting.repetitionPenalty] = newText
    }

    fun onTemperatureType(newText: String) {
        settings.changed.value = true

        val convertedTemperature = newText.toFloatOrNull()
        settings.validations[Setting.temperature] = convertedTemperature != null
        validate()

        settings.values[Setting.temperature] = newText
    }

    fun onTopPType(newText: String) {
        settings.changed.value = true

        val convertedTopP = newText.toFloatOrNull()
        settings.validations[Setting.topP] = convertedTopP != null
        validate()

        settings.values[Setting.topP] = newText
    }

    fun onUserNameType(newText: String) {
        settings.changed.value = true

        settings.validations[Setting.userName] = !newText.isEmpty()
        validate()

        settings.values[Setting.userName] = newText
    }

    fun onBotNameType(newText: String) {
        settings.changed.value = true

        validate()

        settings.values[Setting.botName] = newText
    }

    fun onContextType(newText: String) {
        settings.changed.value = true

        validate()

        settings.values[Setting.context] = newText
    }

    fun onNavigateBackClick() {
        navigateBack()
    }

    fun validate() {
        settings.valid.value = settings.validations.values.all { it }
    }

    fun onApplySettingsClick() {
        settings.changed.value = false

        saveSettings()
    }
    
    fun onDiscardSettingsClick() {
        for(setting in Setting.values()) settings.validations[setting] = true

        settings.valid.value = true
        settings.changed.value = false

        loadSettings()
    }

    fun saveSettings() {
        scope.launch(Dispatchers.Main) {
            context.dataStore.edit { preferences ->
                for(setting in Setting.values()) {
                    if(settings.validations[setting]!!) {
                        preferences[settings.keys[setting] as Preferences.Key<Any>] = settings.values[setting]!!
                    }
                }
            }
        }
    }

    fun loadSettings() {
        scope.launch(Dispatchers.Main) {
            val data = context.dataStore.data.first()

            for(setting in Setting.values()) {
                val key = settings.keys[setting] as Preferences.Key<Any>
                settings.values[setting] = (data[key] ?: settings.defaults[setting] ?: "").toString()
                settings.validations[setting] = true
            }
        }
    }
}