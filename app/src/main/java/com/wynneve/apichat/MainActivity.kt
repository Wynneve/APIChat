package com.wynneve.apichat

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.toMutableStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.wynneve.apichat.viewmodels.SettingsViewModel
import com.google.accompanist.navigation.animation.navigation
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.wynneve.apichat.db.ApplicationDatabase
import com.wynneve.apichat.db.Settings
import com.wynneve.apichat.db.controllers.ChatController
import com.wynneve.apichat.db.controllers.ChatSettingController
import com.wynneve.apichat.db.controllers.GlobalSettingController
import com.wynneve.apichat.db.controllers.ProfileController
import com.wynneve.apichat.db.controllers.SettingController
import com.wynneve.apichat.db.entities.DbChat
import com.wynneve.apichat.db.entities.DbChatSetting
import com.wynneve.apichat.db.entities.DbGlobalSetting
import com.wynneve.apichat.screens.ChatScreen
import com.wynneve.apichat.screens.ChatsScreen
import com.wynneve.apichat.screens.NewProfileScreen
import com.wynneve.apichat.screens.ProfileSettingsScreen
import com.wynneve.apichat.screens.ProfilesScreen
import com.wynneve.apichat.screens.SettingsChooseScreen
import com.wynneve.apichat.screens.SettingsScreen
import com.wynneve.apichat.ui.theme.APIChatTheme
import com.wynneve.apichat.viewmodels.ChatViewModel
import com.wynneve.apichat.viewmodels.ChatsViewModel
import com.wynneve.apichat.viewmodels.NewProfileViewModel
import com.wynneve.apichat.viewmodels.ProfileSettingsViewModel
import com.wynneve.apichat.viewmodels.ProfilesViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            APIChatTheme {
                val context = applicationContext
                ApplicationDatabase.initialize(context)

                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .systemBarsPadding()
                        .imePadding(),
                    color=MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberAnimatedNavController()

                    AnimatedNavHost(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = MaterialTheme.colorScheme.background),
                        navController = navController,
                        startDestination = "profiles",
                        enterTransition = {
                            slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(500))
                        },
                        exitTransition = {
                            slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(500))
                        },
                        popEnterTransition = {
                            slideIntoContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(500))
                        },
                        popExitTransition = {
                            slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(500))
                        }
                    ) {
                        composable(
                            "profiles",
                        ) {
                            val profilesViewModel = viewModel { ProfilesViewModel(
                                navigateToNewProfile = { callback ->
                                    navController.navigate("newProfile")
                                    callback()
                                },
                                navigateToChats = { id, callback ->
                                    navController.navigate("profiles/${id}/chats") {
                                        popUpTo(0) { inclusive = true }
                                    }
                                    callback()
                                }
                            )}

                            ProfilesScreen(profilesViewModel = profilesViewModel)
                        }
                        composable("newProfile") {
                            val newProfileViewModel = viewModel { NewProfileViewModel(
                                navigateBack = {
                                    navController.navigateUp()
                                },
                                navigateToChats = { id, callback ->
                                    navController.navigate(
                                        route = "profiles/${id}/chats",
                                        navOptions = NavOptions.Builder()
                                            .setPopUpTo(route="profiles", inclusive = true)
                                            .build()
                                    )
                                    callback()
                                },
                            )}

                            NewProfileScreen(newProfileViewModel = newProfileViewModel)
                        }
                        composable(
                            route = "profiles/{id}/chats",
                            arguments = listOf(navArgument("id") { type = NavType.IntType})
                        ) {
                            val id = it.arguments!!.getInt("id")
                            print(id)
                            val chatsViewModel = viewModel(key=id.toString()) { ChatsViewModel(
                                profileId = id,
                                navigateToChat = { id, callback ->
                                    navController.navigate(route = "chats/${id}")
                                    callback()
                                },
                                navigateToSettings = {
                                    navController.navigate(route = "profiles/${id}/settings")
                                }
                            )}

                            ChatsScreen(chatsViewModel = chatsViewModel)
                        }
                        composable(
                            route = "chats/{id}",
                            arguments = listOf(navArgument("id") { type = NavType.IntType})
                        ) {
                            val id = it.arguments!!.getInt("id")
                            print(id)
                            val chatViewModel = viewModel(key=id.toString()) { ChatViewModel(
                                chatId = id,
                                navigateToSettings = {
                                    navController.navigate(route = "chats/${id}/settings")
                                    println("navigated")
                                },
                                navigateBack = {
                                    navController.navigateUp()
                                },
                                loadChat = { callback ->
                                    MainScope().launch(Dispatchers.IO) {
                                        val chat = ChatController.getChatById(id).first()!!

                                        MainScope().launch(Dispatchers.Main) { callback(chat) }
                                    }
                                },
                                saveChat = { chat, callback ->
                                    MainScope().launch(Dispatchers.IO) {
                                        ChatController.updateChat(chat)

                                        MainScope().launch(Dispatchers.Main) { callback() }
                                    }
                                }
                            )
                            }

                            ChatScreen(chatViewModel = chatViewModel)
                        }
                        composable(
                            route = "chats/{id}/settings",
                            arguments = listOf(navArgument("id") { type = NavType.IntType})
                        ) {
                            val id = it.arguments!!.getInt("id")
                            print(id)
                            val settingsViewModel = viewModel(key=id.toString()) { SettingsViewModel(
                                loadSettings = { callback ->
                                    MainScope().launch(Dispatchers.IO) {
                                        val settings = Settings.values().map { setting ->
                                            Pair(
                                                setting,
                                                ChatSettingController.getChatSettingByChatAndId(
                                                    id,
                                                    SettingController.settingsIds[setting]!!
                                                ).first()!!.value
                                            )
                                        }.toMutableStateMap()

                                        MainScope().launch(Dispatchers.Main) { callback(settings) }
                                    }
                                },
                                saveSettings = { settings, callback ->
                                    MainScope().launch(Dispatchers.IO) {
                                        settings.forEach { entry ->
                                            ChatSettingController.updateChatSetting(DbChatSetting(
                                                chatId = id,
                                                settingId = SettingController.settingsIds[entry.key]!!,
                                                value = entry.value
                                            ))
                                        }

                                        MainScope().launch(Dispatchers.Main) { callback() }
                                    }
                                },
                                navigateBack = {
                                    navController.navigateUp()
                                }
                            )}

                            SettingsScreen(settingsViewModel = settingsViewModel)
                        }
                        composable(
                            route = "profiles/{id}/settings",
                            arguments = listOf(navArgument("id") { type = NavType.IntType})
                        ) {
                            val id = it.arguments!!.getInt("id")
                            print(id)

                            SettingsChooseScreen(
                                navigateToProfileSettings = {
                                    navController.navigate(route = "profiles/${id}/settings/profile")
                                },
                                navigateToGlobalSettings = {
                                    navController.navigate(route = "profiles/${id}/settings/global")
                                },
                                navigateBack = {
                                    navController.navigateUp()
                                }
                            )
                        }
                        composable(
                            route="profiles/{id}/settings/profile",
                            arguments = listOf(navArgument("id") { type = NavType.IntType})
                        ) {
                            val id = it.arguments!!.getInt("id")
                            print(id)
                            val profileSettingsViewModel = viewModel(key=id.toString()) { ProfileSettingsViewModel(
                                loadProfile = { callback ->
                                    MainScope().launch(Dispatchers.IO) {
                                        val profile = ProfileController.getProfileById(id).first()!!

                                        MainScope().launch(Dispatchers.Main) { callback(profile) }
                                    }
                                },
                                saveProfile = { profile, callback ->
                                    MainScope().launch(Dispatchers.IO) {
                                        ProfileController.updateProfile(profile)

                                        MainScope().launch(Dispatchers.Main) { callback() }
                                    }
                                },
                                navigateToProfiles = {
                                    navController.navigate("profiles") {
                                        popUpTo(0) { inclusive = true }
                                    }
                                },
                                navigateBack = {
                                    navController.navigateUp()
                                }
                            )}

                            ProfileSettingsScreen(profileSettingsViewModel = profileSettingsViewModel)
                        }
                        composable(
                            route="profiles/{id}/settings/global",
                            arguments = listOf(navArgument("id") { type = NavType.IntType})
                        ) {
                            val id = it.arguments!!.getInt("id")
                            print(id)
                            val settingsViewModel = viewModel(key=id.toString()) { SettingsViewModel(
                                loadSettings = { callback ->
                                    MainScope().launch(Dispatchers.IO) {
                                        val settings = Settings.values().map { setting ->
                                            Pair(
                                                setting,
                                                GlobalSettingController.getGlobalSettingByProfileAndId(
                                                    id,
                                                    SettingController.settingsIds[setting]!!
                                                ).first()!!.value
                                            )
                                        }.toMutableStateMap()

                                        MainScope().launch(Dispatchers.Main) { callback(settings) }
                                    }
                                },
                                saveSettings = { settings, callback ->
                                    MainScope().launch(Dispatchers.IO) {
                                        settings.forEach { entry ->
                                            GlobalSettingController.updateGlobalSetting(DbGlobalSetting(
                                                profileId = id,
                                                settingId = SettingController.settingsIds[entry.key]!!,
                                                value = entry.value
                                            ))
                                        }

                                        MainScope().launch(Dispatchers.Main) { callback() }
                                    }
                                },
                                navigateBack = {
                                    navController.navigateUp()
                                }
                            )
                            }

                            SettingsScreen(settingsViewModel = settingsViewModel)
                        }
                    }
                }
            }
        }
    }
}