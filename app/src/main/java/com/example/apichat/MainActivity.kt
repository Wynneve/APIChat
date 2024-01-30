package com.example.apichat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.apichat.ui.theme.APIChatTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //chat.messages.add(Message("user", Date(2024, 1, 1), "Hello! Could you please write a program in Python for me?"))
        //chat.messages.add(Message("bot", Date(2024, 1, 1), "No, I am stoopid ;("))

        setContent {
            APIChatTheme() {
                val scope = rememberCoroutineScope()
                val context = applicationContext

                val navController = rememberNavController()

                val settings = Settings()
                val settingsController = SettingsController(settings, context, scope, { navController.navigateUp() })

                val chat = Chat(settings, context, scope, {navController.navigate(route = "settings")})

                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color=MaterialTheme.colorScheme.background
                ) {
                    NavHost(navController = navController, startDestination = "chat") {
                        composable("chat"
                        ) {
                            ChatScreen(chat = chat)
                        }
                        composable("settings") {
                            SettingsScreen(settings = settings, controller = settingsController)
                        }
                    }
                }
            }
        }
    }
}