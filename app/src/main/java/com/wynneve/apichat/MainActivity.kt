package com.wynneve.apichat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavOptions
import com.google.accompanist.navigation.animation.navigation
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.wynneve.apichat.db.ApplicationDatabase
import com.wynneve.apichat.screens.ChatsScreen
import com.wynneve.apichat.screens.NewProfileScreen
import com.wynneve.apichat.screens.ProfilesScreen
import com.wynneve.apichat.ui.theme.APIChatTheme
import com.wynneve.apichat.viewmodels.ChatsViewModel
import com.wynneve.apichat.viewmodels.NewProfileViewModel
import com.wynneve.apichat.viewmodels.ProfilesViewModel

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //chat.messages.add(Message("user", Date(2024, 1, 1), "Hello! Could you please write a program in Python for me?"))
        //chat.messages.add(Message("bot", Date(2024, 1, 1), "No, I am stoopid ;("))

        setContent {
            APIChatTheme {
                val context = applicationContext
                ApplicationDatabase.initialize(context)

                val navController = rememberAnimatedNavController()

                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color=MaterialTheme.colorScheme.background
                ) {
                    AnimatedNavHost(
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
                                    navController.navigate(
                                        route = "chats/${id}",
                                        navOptions = NavOptions.Builder()
                                            .setEnterAnim(0)
                                            .setExitAnim(0)
                                            .setPopUpTo("profiles", true)
                                            .build()
                                    )
                                    callback()
                                }
                            )}

                            ProfilesScreen(profilesViewModel = profilesViewModel)
                        }
                        composable("newProfile") {
                            val newProfileViewModel = viewModel { NewProfileViewModel(
                                navigateBack = { callback ->
                                    navController.navigateUp()
                                    callback()
                                },
                                navigateToChats = { id, callback ->
                                    navController.navigate(
                                        route = "chats/${id}",
                                        navOptions = NavOptions.Builder()
                                            .setEnterAnim(0)
                                            .setExitAnim(0)
                                            .setPopUpTo("profiles", true)
                                            .build()
                                    )
                                    //navController.popBackStack("profiles", inclusive = false)
                                    callback()
                                },
                            )}

                            NewProfileScreen(newProfileViewModel = newProfileViewModel)
                        }
                        composable("chats/{id}") {
                            val id = it.arguments!!.getInt("id")
                            val chatsViewModel = viewModel { ChatsViewModel(
                                profileId = id,
                                navigateToChat = { id, callback -> }
                            )}

                            ChatsScreen(chatsViewModel = chatsViewModel)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EnterAnimation(content: @Composable () -> Unit) {
    AnimatedVisibility(
        visibleState = MutableTransitionState(
            initialState = false
        ).apply { targetState = true },
        modifier = Modifier,
        enter = slideInVertically(
            initialOffsetY = { -40 }
        ) + expandVertically(
            expandFrom = Alignment.Top
        ) + fadeIn(initialAlpha = 0.3f),
        exit = slideOutVertically() + shrinkVertically() + fadeOut(),
    ) {
        content()
    }
}