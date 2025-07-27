package com.wynneve.apichat.db
import com.wynneve.apichat.R

enum class SettingType {
    url,
    string,
    text,
    integer,
    float,
}

enum class SettingCategory(val _name: Int) {
    Request(R.string.settings_RequestSettings),
    Generation(R.string.settings_GenerationSettings),
    Dialogue(R.string.settings_DialogueSettings)
}

fun validateSetting(value: String, type: SettingType): Boolean {
    return when(type) {
        SettingType.url -> {
            validateUrl(value)
        }
        SettingType.string -> {
            validateString(value)
        }
        SettingType.text -> {
            validateString(value)
        }
        SettingType.integer -> {
            validateInteger(value)
        }
        SettingType.float -> {
            validateFloat(value)
        }
    }
}

fun deserializeSetting(value: String, type: SettingType): Any {
    return when(type) {
        SettingType.url -> {
            value
        }
        SettingType.string -> {
            value
        }
        SettingType.text -> {
            value
        }
        SettingType.integer -> {
            value.toInt()
        }
        SettingType.float -> {
            value.toFloat()
        }
    }
}

private val urlRegex = Regex("^https?://[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)+(:[0-9]+)?([/?].*)?\$")
private fun validateUrl(value: String): Boolean {
    return urlRegex.matches(value)
}
private fun validateString(value: String): Boolean {
    return true
}
private fun validateInteger(value: String): Boolean {
    return value.toIntOrNull() != null
}
private fun validateFloat(value: String): Boolean {
    return value.toFloatOrNull() != null
}

enum class Settings(val _name: String, val _default: String, val _description: Int, val _category: SettingCategory, val _type: SettingType) {
    ApiEndpoint("api_endpoint", "http://0.0.0.0:8080", R.string.settings_ApiEndpoint, SettingCategory.Request, SettingType.url),
    BeginningTemplate("beginning_template", "<|begin_of_text|>", R.string.settings_BeginningTemplate, SettingCategory.Request, SettingType.text),
    MessageTemplate("message_template", "<|start_header_id|>{role}<|end_header_id|>\n\n{content}<|eot_id|>", R.string.settings_MessageTemplate, SettingCategory.Request, SettingType.text),
    UserName("user_name", "user", R.string.settings_UserName, SettingCategory.Request, SettingType.string),
    AssistantName("assistant_name", "assistant", R.string.settings_AssistantName, SettingCategory.Request, SettingType.string),
    SystemName("system_name", "system", R.string.settings_SystemName, SettingCategory.Request, SettingType.string),

    MaxTokens("n_predict", "512", R.string.settings_MaxTokens, SettingCategory.Generation, SettingType.integer),
    RepetitionPenalty("repeat_penalty", "1.15", R.string.settings_RepetitionPenalty, SettingCategory.Generation, SettingType.float),
    Temperature("temperature", "0.7", R.string.settings_Temperature, SettingCategory.Generation, SettingType.float),
    TopP("top_p", "0.9", R.string.settings_TopP, SettingCategory.Generation, SettingType.float),

    // Dialogue Settings
    Context("context", "This is a conversation between the User and LLM-powered AI Assistant.", R.string.settings_Context, SettingCategory.Dialogue, SettingType.text),
}