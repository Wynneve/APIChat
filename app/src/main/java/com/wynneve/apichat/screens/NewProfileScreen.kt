package com.wynneve.apichat.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wynneve.apichat.composables.ContentListColumn
import com.wynneve.apichat.composables.HeaderRow
import com.wynneve.apichat.composables.LoadingPopup
import com.wynneve.apichat.composables.NamedTextField
import com.wynneve.apichat.ui.theme.APIChatTheme
import com.wynneve.apichat.viewmodels.NewProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewProfileScreen(newProfileViewModel: NewProfileViewModel) {
    var navigationEnabled by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
    ) {
        HeaderRow(
            title = "New profile",
            navigation = {
                IconButton(
                    modifier = Modifier
                        .fillMaxHeight(),
                    onClick = {
                        navigationEnabled = false
                        newProfileViewModel.navigateBack {
                            navigationEnabled = true
                        }
                    },
                    enabled = navigationEnabled
                ) {
                    Icon(
                        modifier = Modifier
                            .width(25.dp)
                            .height(25.dp),
                        tint = MaterialTheme.colorScheme.onBackground,
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "",
                    )
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 10.dp)
        ) {
            ContentListColumn {
                NamedTextField(
                    title = "Login",
                    placeholder = "Login",
                    value = { newProfileViewModel.login },
                    onValueChange = newProfileViewModel::loginType,
                )
                NamedTextField(
                    title = "Password",
                    placeholder = "Password",
                    value = { newProfileViewModel.password },
                    onValueChange = newProfileViewModel::passwordType,
                    visualTransformation = PasswordVisualTransformation()
                )

                Spacer(modifier = Modifier)

                Button(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                    ),
                    onClick = newProfileViewModel::createProfileClick
                ) {
                    Text(
                        text = "Create",
                        style = MaterialTheme.typography.displayMedium
                    )
                }
            }
        }
    }

    LoadingPopup(!newProfileViewModel.synced)
}

@Preview
@Composable
fun NewProfileScreenPreview() {
    APIChatTheme {
        val newProfileViewModel = NewProfileViewModel(
            navigateBack = { _ -> },
            navigateToChats = { _, _ -> },
        )

        NewProfileScreen(newProfileViewModel = newProfileViewModel)
    }
}