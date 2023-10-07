package com.example.chatexu.presentation.chat_list.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.chatexu.domain.model.ChatRow
import com.example.chatexu.presentation.chat_list.ChatListViewModel

@Composable
fun ChatListScreen(
    navController: NavController,
    viewModel: ChatListViewModel = hiltViewModel()
) {
    val state = viewModel.state.value

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

        val rows = generateSequence { ChatRow.Builder().fastBuild() }
            .take(50)
            .toList()
        ChatsListLazy(chatRows = rows, navController = navController)

    }

}
