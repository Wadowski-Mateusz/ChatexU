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
import com.example.chatexu.common.Constants
import com.example.chatexu.presentation.Screen
import com.example.chatexu.presentation.auth.AuthScreen
import com.example.chatexu.presentation.auth.AuthScreenDebug
import com.example.chatexu.presentation.chat.ChatScreen
import com.example.chatexu.presentation.chat_list.ChatListScreen
import com.example.chatexu.presentation.create_chat.CreateChatScreen
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
                        startDestination = Screen.AuthScreen.route,
                    ) {
                        composable(
                            route = Screen.AuthScreen.route
                        ) {
                            AuthScreen(navController)
                        }
                        composable(
                            route = Screen.AuthScreenDebug.route
                        ) {
                            AuthScreenDebug(navController)
                        }

                        composable(
                            route = Screen.ChatListScreen.route + "/{${Constants.PARAM_USER_ID}}"
                        ) {
                            ChatListScreen(navController)
                        }

                        composable(
                            route = Screen.ChatScreen.route + "/{${Constants.PARAM_USER_ID}}/{${Constants.PARAM_CHAT_ID}}"
                        ) {
                            ChatScreen(navController)
                        }

                        composable(
                            route = Screen.CreateChatScreen.route + "/{${Constants.PARAM_USER_ID}}"
                        ) {
                            CreateChatScreen(navController)
                        }

                    }
                }
            }
        }
    }
}

