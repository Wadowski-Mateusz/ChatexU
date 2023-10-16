package com.example.chatexu.presentation.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.chatexu.presentation.Screen
import com.example.chatexu.presentation.auth.components.UserItem
import com.example.chatexu.presentation.commons.composable.ScreenName

@Composable
fun AuthScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val state = viewModel.state.value

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Blue)
    ) {
        ScreenName(screenName = "Auth")

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(red = 0.9f, green = 1f, blue = 1f))
        ) {
            items(items = state.users, key = {it.id}) { user ->
                UserItem (
                    user = user,
                    onItemClick = {
                        navController.navigate(Screen.ChatListScreen.route  + "/${user.id}")
                    }
                )
            }
        }
    }
}

