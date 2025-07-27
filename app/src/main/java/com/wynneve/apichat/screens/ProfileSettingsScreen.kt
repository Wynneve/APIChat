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
import com.wynneve.apichat.composables.HeaderRow
import com.wynneve.apichat.composables.NamedTextField
import com.wynneve.apichat.composables.NamedGroup
import com.wynneve.apichat.ui.theme.APIChatTheme
import com.wynneve.apichat.ui.theme.colorInactive
import com.wynneve.apichat.ui.theme.colorLogout
import com.wynneve.apichat.viewmodels.ProfileSettingsViewModel
import com.wynneve.apichat.R

@Composable
fun ProfileSettingsScreen(profileSettingsViewModel: ProfileSettingsViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        HeaderRow(
            title = LocalContext.current.getString(R.string.newProfile_Title),
            navigation = {
                IconButton(
                    modifier = Modifier
                        .size(40.dp),
                    onClick = {
                        profileSettingsViewModel.navigateBack()
                    }
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
                        profileSettingsViewModel.onApplySettingsClick {

                        }
                    },
                    enabled = profileSettingsViewModel.valid && profileSettingsViewModel.changed
                ) {
                    Icon(
                        modifier = Modifier,
                        imageVector = Icons.Default.Check,
                        contentDescription = "Apply",
                        tint =
                        if(profileSettingsViewModel.valid && profileSettingsViewModel.changed) MaterialTheme.colorScheme.onSurface
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
            NamedGroup(title = LocalContext.current.getString(R.string.profileSettings_ProfileSettings), scrollable = false) {
                NamedTextField(
                    title = LocalContext.current.getString(R.string.profileSettings_Login),
                    value = { profileSettingsViewModel.login },
                    onValueChange = { newText ->
                        profileSettingsViewModel.onLoginType(newText)
                    },
                    placeholder = LocalContext.current.getString(R.string.profileSettings_Login),
                )

                NamedTextField(
                    title = LocalContext.current.getString(R.string.profileSettings_Password),
                    value = { profileSettingsViewModel.password },
                    onValueChange = { newText ->
                        profileSettingsViewModel.onPasswordType(newText)
                    },
                    placeholder = LocalContext.current.getString(R.string.profileSettings_Password),
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
                onClick = {
                    profileSettingsViewModel.navigateToProfiles()
                }
            ) {
                Text(
                    text = LocalContext.current.getString(R.string.profileSettings_LogOut),
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

        val profileSettings = viewModel { ProfileSettingsViewModel(
            { },
            { _, _ -> },
            {},
            {}
        ) }

        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ProfileSettingsScreen(profileSettingsViewModel = profileSettings)
        }
    }
}