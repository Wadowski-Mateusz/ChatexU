package com.example.chatexu.presentation

sealed class Screen(val route: String) {
    data object AuthScreen: Screen("auth_screen")
    data object AuthScreenDebug: Screen("auth_screen_debug")
    data object ChatListScreen: Screen("chat_list_screen")
    data object ChatScreen: Screen("chat_screen")
    data object CreateChatScreen: Screen("create_chat_screen")


}