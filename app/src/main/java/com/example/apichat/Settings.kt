package com.example.apichat

import android.content.Context
import android.webkit.URLUtil
import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
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

    val values: Map<Setting, MutableState<String>> = mutableMapOf<Setting, MutableState<String>>().apply {
        defaults.forEach { (setting, value) ->
            this[setting] = mutableStateOf(value)
        }
    },
    val validations: MutableMap<Setting, Boolean> = mutableMapOf<Setting, Boolean>().apply {
        defaults.keys.forEach { setting ->
            this[setting] = true
        }
    },
    val valid: MutableState<Boolean> = mutableStateOf(true),

    val changed: MutableState<Boolean> = mutableStateOf(false)
)

@Stable
class SettingsViewModel(private val context: Context, private val scope: CoroutineScope, val navigateBack: () -> Unit): ViewModel() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private val model = Settings()

    var scrollState: ScrollState? = null

    init {
        loadSettings()
    }

    fun get(setting: Setting): String {
        return model.values[setting]!!.value
    }

    fun set(setting: Setting, value: String) {
        model.values[setting]!!.value = value
    }

    fun getDefault(setting: Setting): String {
        return model.defaults[setting]!!
    }

    fun getValid() : Boolean {
        return model.valid.value
    }

    fun getChanged(): Boolean {
        return model.changed.value
    }


    fun onApiEndpointType(newText: String) {
        model.changed.value = true

        model.validations[Setting.apiEndpoint] = URLUtil.isValidUrl(newText)
        validate()

        model.values[Setting.apiEndpoint]?.value = newText
    }

    fun onMaxTokensType(newText: String) {
        model.changed.value = true

        val convertedMaxTokens = newText.toIntOrNull()
        model.validations[Setting.maxTokens] = convertedMaxTokens != null
        validate()

        model.values[Setting.maxTokens]?.value = newText
    }

    fun onRepetitionPenaltyType(newText: String) {
        model.changed.value = true

        val convertedRepetitionPenalty = newText.toFloatOrNull()
        model.validations[Setting.repetitionPenalty] = convertedRepetitionPenalty != null
        validate()

        model.values[Setting.repetitionPenalty]?.value = newText
    }

    fun onTemperatureType(newText: String) {
        model.changed.value = true

        val convertedTemperature = newText.toFloatOrNull()
        model.validations[Setting.temperature] = convertedTemperature != null
        validate()

        model.values[Setting.temperature]?.value = newText
    }

    fun onTopPType(newText: String) {
        model.changed.value = true

        val convertedTopP = newText.toFloatOrNull()
        model.validations[Setting.topP] = convertedTopP != null
        validate()

        model.values[Setting.topP]?.value = newText
    }

    fun onUserNameType(newText: String) {
        model.changed.value = true

        model.validations[Setting.userName] = newText.isNotEmpty()
        validate()

        model.values[Setting.userName]?.value = newText
    }

    fun onBotNameType(newText: String) {
        model.changed.value = true

        validate()

        model.values[Setting.botName]?.value = newText
    }

    fun onContextType(newText: String) {
        model.changed.value = true

        validate()

        model.values[Setting.context]?.value = newText
    }

    fun onNavigateBackClick() {
        navigateBack()
    }

    fun validate() {
        model.valid.value = model.validations.values.all { it }
    }

    fun onApplySettingsClick() {
        model.changed.value = false

        saveSettings()
    }

    fun onDiscardSettingsClick() {
        for(setting in Setting.values()) model.validations[setting] = true

        model.valid.value = true
        model.changed.value = false

        loadSettings()
    }

    fun saveSettings() {
        scope.launch(Dispatchers.Main) {
            context.dataStore.edit { preferences ->
                for(setting in Setting.values()) {
                    if(model.validations[setting]!!) {
                        preferences[model.keys[setting] as Preferences.Key<Any>] = model.values[setting]!!.value
                    }
                }
            }
        }
    }

    fun loadSettings() {
        scope.launch(Dispatchers.Main) {
            val data = context.dataStore.data.first()

            for(setting in Setting.values()) {
                val key = model.keys[setting] as Preferences.Key<Any>
                model.values[setting]?.value = (data[key] ?: model.defaults[setting] ?: "").toString()
                model.validations[setting] = true
            }
        }
    }
}