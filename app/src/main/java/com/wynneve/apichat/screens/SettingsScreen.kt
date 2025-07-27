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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wynneve.apichat.R
import com.wynneve.apichat.viewmodels.SettingsViewModel
import com.wynneve.apichat.composables.HeaderRow
import com.wynneve.apichat.composables.NamedGroup
import com.wynneve.apichat.composables.NamedTextField
import com.wynneve.apichat.db.SettingCategory
import com.wynneve.apichat.db.SettingType
import com.wynneve.apichat.db.Settings
import com.wynneve.apichat.ui.theme.APIChatTheme
import com.wynneve.apichat.ui.theme.colorInactive
import kotlinx.coroutines.delay

@Composable
fun SettingsScreen(settingsViewModel: SettingsViewModel) {
    var navigationEnabled by remember { mutableStateOf(true) }
    LaunchedEffect(navigationEnabled) {
        delay(1000)
        navigationEnabled = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        HeaderRow(
            title = LocalContext.current.getString(R.string.settings_Title),
            navigation = {
                IconButton(
                    modifier = Modifier
                        .size(40.dp),
                    onClick = {
                        navigationEnabled = false
                        settingsViewModel.navigateBack()
                    },
                    enabled = navigationEnabled
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
                    onClick = {
                        settingsViewModel.synced = false
                        settingsViewModel.onSaveClick {
                            settingsViewModel.synced = true
                        }
                    },
                    enabled = settingsViewModel.valid && settingsViewModel.changed
                ) {
                    Icon(
                        modifier = Modifier,
                        imageVector = Icons.Default.Check,
                        contentDescription = "Apply",
                        tint =
                        if(settingsViewModel.valid && settingsViewModel.changed) MaterialTheme.colorScheme.onSurface
                        else colorInactive
                    )
                }
            }
        )

        Column(
            modifier = Modifier
                .padding(10.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            SettingCategory.values().forEach {category ->
                NamedGroup(title = LocalContext.current.getString(category._name), scrollable = false) {
                    Settings.values().filter {
                        setting -> setting._category == category
                    }.forEach { setting ->
                        NamedTextField(
                            title = LocalContext.current.getString(setting._description),
                            value = { settingsViewModel.settings?.get(setting) ?: "" },
                            onValueChange = { newText ->
                                settingsViewModel.onSettingType(setting, newText)
                            },
                            placeholder = setting._default,
                            minLines = if(setting._type != SettingType.text) 1 else 5,
                            maxLines = if(setting._type != SettingType.text) 1 else 5,
                            singleLine = setting._type != SettingType.text
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun ChatSettingsScreenPreview() {
    APIChatTheme {
        val settingsViewModel = viewModel { SettingsViewModel(
            loadSettings = { mutableStateMapOf<Settings, String>() },
            saveSettings = { _, _ -> },
            navigateBack = {}
        ) }

        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            SettingsScreen(settingsViewModel = settingsViewModel)
        }
    }
}