package com.example.chatexu.presentation.chat_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.chatexu.presentation.Screen
import com.example.chatexu.presentation.chat_list.components.ChatListItem
import com.example.chatexu.presentation.commons.composable.ScreenName

@Composable
fun ChatListScreen(
    navController: NavController,
    viewModel: ChatListViewModel = hiltViewModel()
) {
    val state = viewModel.state.value

    Column(
        modifier = Modifier.fillMaxSize().background(Color.LightGray)
    ) {
//        ChatsListLazy(chatRows = state.chatRows, navController = navController)
        ScreenName(screenName = "Chat List")
        LazyColumn(modifier = Modifier.fillMaxWidth().background(Color.Magenta)) {
            items(items = state.chatRows, key = {it.chatId}) { chatRow ->
                ChatListItem(
                    chatRow = chatRow,
                    onItemClick = {
                        navController.navigate(Screen.ChatScreen.route + "/${state.userId}/${chatRow.chatId}")
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
