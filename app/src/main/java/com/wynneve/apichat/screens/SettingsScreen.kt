package com.wynneve.apichat.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wynneve.apichat.composables.HeaderRow
import com.wynneve.apichat.composables.NamedGroup
import com.wynneve.apichat.composables.NamedTextField
import com.wynneve.apichat.ui.theme.APIChatTheme
import com.wynneve.apichat.ui.theme.colorInactive

@Composable
fun SettingsScreen(settings: SettingsViewModel) {
    settings.scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        HeaderRow(
            title = "Settings",
            navigation = {
                IconButton(
                    modifier = Modifier
                        .size(40.dp),
                    onClick = {}
                ) {
                    Icon(
                        modifier = Modifier
                            .size(25.dp),
                        imageVector = Icons.Default.ArrowBack,
                        tint = MaterialTheme.colorScheme.onSurface,
                        contentDescription = "Back",
                    )
                }
            },
            actions = {
                IconButton(
                    modifier = Modifier
                        .height(40.dp),
                    onClick = settings::onApplySettingsClick,
                    enabled = settings.getValid() && settings.getChanged()
                ) {
                    Icon(
                        modifier = Modifier,
                        imageVector = Icons.Default.Check,
                        contentDescription = "Apply",
                        tint =
                        if(settings.getValid() && settings.getChanged()) MaterialTheme.colorScheme.onSurface
                        else colorInactive
                    )
                }

                IconButton(
                    modifier = Modifier
                        .height(40.dp),
                    onClick = settings::onDiscardSettingsClick,
                    enabled = settings.getChanged()
                ) {
                    Icon(
                        modifier = Modifier,
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Discard",
                        tint =
                        if(settings.getChanged()) MaterialTheme.colorScheme.onSurface
                        else colorInactive
                    )
                }
            }
        )

        Column(
            modifier = Modifier
                .padding(10.dp)
                .verticalScroll(settings.scrollState!!),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            NamedGroup(title = "API and LLM settings", scrollable = false) {
                NamedTextField(
                    title = "API Endpoint",
                    value = { settings.get(Setting.apiEndpoint) },
                    onValueChange = settings::onApiEndpointType,
                    placeholder = settings.getDefault(Setting.apiEndpoint),
                )

                NamedTextField(
                    title = "Maximum tokens",
                    value = { settings.get(Setting.maxTokens) },
                    onValueChange = settings::onMaxTokensType,
                    placeholder = settings.getDefault(Setting.maxTokens),
                )

                NamedTextField(
                    title = "Repetition penalty",
                    value = { settings.get(Setting.repetitionPenalty) },
                    onValueChange = settings::onRepetitionPenaltyType,
                    placeholder = settings.getDefault(Setting.repetitionPenalty),
                )

                NamedTextField(
                    title = "Temperature",
                    value = { settings.get(Setting.temperature) },
                    onValueChange = settings::onTemperatureType,
                    placeholder = settings.getDefault(Setting.temperature),
                )

                NamedTextField(
                    title = "Top P",
                    value = { settings.get(Setting.topP) },
                    onValueChange = settings::onTopPType,
                    placeholder = settings.getDefault(Setting.topP),
                )
            }

            NamedGroup(title = "Dialogue settings", scrollable = false) {
                NamedTextField(
                    title = "User name",
                    value = { settings.get(Setting.userName) },
                    onValueChange = settings::onUserNameType,
                    placeholder = settings.getDefault(Setting.userName),
                )

                NamedTextField(
                    title = "Bot name",
                    value = { settings.get(Setting.botName) },
                    onValueChange = settings::onBotNameType,
                    placeholder = settings.getDefault(Setting.botName),
                )

                NamedTextField(
                    title = "Context",
                    value = { settings.get(Setting.context) },
                    onValueChange = settings::onContextType,
                    placeholder = settings.getDefault(Setting.context),
                    singleLine = false,
                    minLines = 5,
                    maxLines = 5
                )
            }
        }
    }
}

@Composable
@Preview
fun ChatSettingsScreenPreview() {
    APIChatTheme {
        val scope = rememberCoroutineScope()
        val context = LocalContext.current

        val settings = viewModel { SettingsViewModel(context, scope, {}) }

        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ChatSettingsScreen(settings = settings)
        }
    }
}