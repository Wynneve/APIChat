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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wynneve.apichat.composables.ContentListColumn
import com.wynneve.apichat.composables.HeaderRow
import com.wynneve.apichat.composables.LoadingPopup
import com.wynneve.apichat.composables.NamedTextField
import com.wynneve.apichat.ui.theme.APIChatTheme
import com.wynneve.apichat.viewmodels.NewProfileViewModel
import kotlinx.coroutines.delay
import com.wynneve.apichat.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewProfileScreen(newProfileViewModel: NewProfileViewModel) {
    var navigationEnabled by remember { mutableStateOf(true) }
    LaunchedEffect(navigationEnabled) {
        delay(1000)
        navigationEnabled = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
    ) {
        HeaderRow(
            title = LocalContext.current.getString(R.string.newProfile_Title),
            navigation = {
                IconButton(
                    modifier = Modifier
                        .fillMaxHeight(),
                    onClick = {
                        navigationEnabled = false
                        newProfileViewModel.navigateBack()
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
                    title = LocalContext.current.getString(R.string.newProfile_Login),
                    placeholder = LocalContext.current.getString(R.string.newProfile_Login),
                    value = { newProfileViewModel.login },
                    onValueChange = newProfileViewModel::loginType,
                )
                NamedTextField(
                    title = LocalContext.current.getString(R.string.newProfile_Password),
                    placeholder = LocalContext.current.getString(R.string.newProfile_Password),
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
                        text = LocalContext.current.getString(R.string.newProfile_Create),
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
            navigateToChats = { _, _ -> },
            navigateBack = { },
        )

        NewProfileScreen(newProfileViewModel = newProfileViewModel)
    }
}