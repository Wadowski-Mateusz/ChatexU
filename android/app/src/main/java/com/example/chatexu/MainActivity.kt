package com.example.chatexu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chatexu.presentation.Screen
import com.example.chatexu.presentation.chat.ChatScreen
import com.example.chatexu.presentation.chat_list.ChatListScreen
import com.example.chatexu.ui.theme.ChatexUTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChatexUTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.ChatListScreen.route,
                    ) {
                        composable(
                            route = Screen.ChatListScreen.route
                        ) {
                            ChatListScreen(navController)
                        }

                        composable(
                            route = Screen.ChatScreen.route + "/{chatId}"
                        ) {
                            ChatScreen(navController)
                        }

                    }
                }
            }
        }
    }
}

