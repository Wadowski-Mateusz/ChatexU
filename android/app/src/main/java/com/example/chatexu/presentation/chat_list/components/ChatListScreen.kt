package com.example.chatexu.presentation.chat_list.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.chatexu.presentation.Screen
import com.example.chatexu.presentation.chat_list.ChatListViewModel

@Composable
fun ChatListScreen(
    navController: NavController,
    viewModel: ChatListViewModel = hiltViewModel()
) {
    val state = viewModel.state.value

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
//        ChatsListLazy(chatRows = state.chatRows, navController = navController)

        LazyColumn() {
            items(items = state.chatRows, key = {it.chatId}) { chatRow ->
                ChatListItem(
                    chatRow = chatRow,
                    onItemClick = {
                        navController.navigate(Screen.ChatScreen.route + "/${chatRow.chatId}")
                    }
                )
            }
        }

        if(state.error.isNotBlank()) {
            Text(
                text = state.error,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)

            )
        }
    }


}
