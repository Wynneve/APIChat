package com.wynneve.apichat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.MutableTransitionState
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.wynneve.apichat.ui.theme.APIChatTheme
//import com.example.apichat.viewmodels.Chat
//import com.example.apichat.viewmodels.ChatController

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //chat.messages.add(Message("user", Date(2024, 1, 1), "Hello! Could you please write a program in Python for me?"))
        //chat.messages.add(Message("bot", Date(2024, 1, 1), "No, I am stoopid ;("))

        setContent {
            APIChatTheme {
                val scope = rememberCoroutineScope()
                val context = applicationContext

                val navController: NavHostController = rememberNavController()

                val settingsViewModel = viewModel { SettingsViewModel(context, scope, navController::navigateUp) }

                //val chat = Chat()
                //val chatController = ChatController(chat, settingsViewModel, context, scope, { navController.navigate(route = "settings") })

                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color=MaterialTheme.colorScheme.background
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = "chat",
                    ) {
                        composable(
                            "chat",
                        ) {
                            EnterAnimation {
                                //ChatScreen(chat = chat, controller = chatController, settings = settingsViewModel)
                            }
                        }
                        composable("settings") {
                            EnterAnimation {
                                SettingsScreen(settings = settingsViewModel)
                            }
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

@Composable
fun HeaderRow(
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    shape: Shape = RectangleShape,
    horizontalPadding: Dp = 5.dp,
    verticalPadding: Dp = 5.dp,
    arrangement: Arrangement.HorizontalOrVertical = Arrangement.SpaceBetween,
    content: @Composable () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = backgroundColor, shape = shape)
            .padding(horizontal = horizontalPadding, vertical = verticalPadding),
        horizontalArrangement = arrangement
    ) {
        content()
    }
}