package com.wynneve.apichat.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wynneve.apichat.Setting
import com.wynneve.apichat.SettingsViewModel
import com.wynneve.apichat.composables.HeaderRow
import com.wynneve.apichat.composables.NamedTextField
import com.wynneve.apichat.composables.NamedGroup
import com.wynneve.apichat.ui.theme.APIChatTheme
import com.wynneve.apichat.ui.theme.colorInactive
import com.wynneve.apichat.ui.theme.colorLogout

@Composable
fun ProfileSettingsScreen(settings: SettingsViewModel) {
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
            NamedGroup(title = "Profile settings") {
                NamedTextField(
                    title = "Login",
                    value = { settings.get(Setting.userName) },
                    onValueChange = settings::onUserNameType,
                    placeholder = settings.getDefault(Setting.userName),
                )

                NamedTextField(
                    title = "Password",
                    value = { settings.get(Setting.botName) },
                    onValueChange = settings::onBotNameType,
                    placeholder = settings.getDefault(Setting.botName),
                    visualTransformation = PasswordVisualTransformation()
                )
            }

            Button(
                modifier = Modifier
                    .height(40.dp)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                ),
                onClick = {}
            ) {
                Text(
                    text = "Log out",
                    style = MaterialTheme.typography.displayMedium.copy(color = colorLogout)
                )
            }
        }
    }
}

@Composable
@Preview
fun ProfileSettingsScreenPreview() {
    APIChatTheme {
        val scope = rememberCoroutineScope()
        val context = LocalContext.current

        val settings = viewModel { SettingsViewModel(context, scope, {}) }

        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ProfileSettingsScreen(settings = settings)
        }
    }
}