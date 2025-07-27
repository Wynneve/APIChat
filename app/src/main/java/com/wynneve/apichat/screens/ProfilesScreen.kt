package com.wynneve.apichat.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wynneve.apichat.composables.ContentListColumn
import com.wynneve.apichat.composables.HeaderRow
import com.wynneve.apichat.composables.LoadingPopup
import com.wynneve.apichat.composables.NamedTextField
import com.wynneve.apichat.db.ApplicationDatabase
import com.wynneve.apichat.db.entities.DbProfile
import com.wynneve.apichat.ui.theme.APIChatTheme
import com.wynneve.apichat.ui.theme.colorShadow
import com.wynneve.apichat.viewmodels.ProfilesViewModel
import com.wynneve.apichat.R

@Composable
fun ProfilesScreen(profilesViewModel: ProfilesViewModel) {
    var loggingIn by remember { mutableStateOf(false) }

    BackHandler(enabled = loggingIn) {
        loggingIn = false
    }

    val profiles by profilesViewModel.profiles.collectAsState(initial = emptyList())
    var navigationEnabled by remember { mutableStateOf(true) }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
    ) {
        HeaderRow(
            title = LocalContext.current.getString(R.string.profiles_Title),
            actions = {
                IconButton(
                    modifier = Modifier
                        .size(40.dp),
                    onClick = {
                        navigationEnabled = false
                        profilesViewModel.navigateToNewProfile {
                            navigationEnabled = true
                        }
                    },
                    enabled = navigationEnabled
                ) {
                    Icon(
                        modifier = Modifier
                            .width(30.dp)
                            .height(30.dp),
                        tint = MaterialTheme.colorScheme.onBackground,
                        imageVector = Icons.Default.Add,
                        contentDescription = "",
                    )
                }
            }
        )

        if(profiles.isEmpty()) return

        ContentListColumn {
            for(profile in profiles) {
                ProfileEntry(profile) {
                    profilesViewModel.selectedProfile = profile
                    loggingIn = true
                }
            }
        }
    }

    val animatedColor by animateColorAsState(
        targetValue = if(loggingIn) colorShadow else Color.Transparent,
        animationSpec = tween(durationMillis = 250, easing = LinearEasing)
    )
    if(loggingIn) Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = animatedColor)
            .clickable(
                enabled = true,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                loggingIn = false
                profilesViewModel.password = ""
            },
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(fraction = (2f / 3f))
                .clickable(enabled = false) {}
        ) {
            ContentListColumn {
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    text = profilesViewModel.selectedProfile!!.login
                )

                NamedTextField(
                    title = LocalContext.current.getString(R.string.profiles_EnterPassword),
                    placeholder = LocalContext.current.getString(R.string.profiles_Password),
                    value = { profilesViewModel.password },
                    onValueChange = { profilesViewModel.password = it },
                    visualTransformation = PasswordVisualTransformation()
                )

                Spacer(modifier = Modifier)

                val invalidPassword = LocalContext.current.getString(R.string.profiles_InvalidPassword)
                Button(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .height(40.dp)
                        .fillMaxWidth(fraction = (2f / 3f)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                    ),
                    onClick = {
                        navigationEnabled = false
                        profilesViewModel.loginClick(successCallback = {
                            navigationEnabled = true
                            profilesViewModel.password = ""
                        } , failureCallback = {
                            navigationEnabled = true
                            Toast.makeText(context, invalidPassword, Toast.LENGTH_LONG).show()
                        })
                    },
                    enabled = navigationEnabled
                ) {
                    Text(
                        text = LocalContext.current.getString(R.string.profiles_LogIn),
                        style = MaterialTheme.typography.displayMedium
                    )
                }
            }
        }
    }

    LoadingPopup(!profilesViewModel.synced)
}

@Composable
fun ProfileEntry(profile: DbProfile, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(5.dp)
            )
            .height(40.dp)
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .padding(start = 10.dp),
            style = MaterialTheme.typography.displayMedium,
            text = profile.login
        )

        Icon(
            modifier = Modifier
                .padding(end = 5.dp)
                .size(25.dp),
            tint = MaterialTheme.colorScheme.onBackground,
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = "",
        )
    }
}

@Preview
@Composable
fun ProfilesScreenPreview() {
    APIChatTheme {
        val profilesViewModel = ProfilesViewModel(
            navigateToNewProfile = { _ -> },
            navigateToChats = { _, _ -> }
        )

        ProfilesScreen(profilesViewModel)
    }
}