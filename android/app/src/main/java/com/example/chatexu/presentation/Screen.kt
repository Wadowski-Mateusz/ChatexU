package com.example.chatexu.presentation

sealed class Screen(val route: String) {
    data object ChatListScreen: Screen("chat_list_screen")
    data object ChatScreen: Screen("chat_screen")
}