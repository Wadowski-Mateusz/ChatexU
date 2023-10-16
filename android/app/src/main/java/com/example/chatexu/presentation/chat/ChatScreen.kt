package com.example.chatexu.presentation.chat


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.chatexu.domain.model.MessageType
import com.example.chatexu.presentation.chat.components.ChatMessageItem
import com.example.chatexu.presentation.commons.composable.ScreenName

@Composable
fun ChatScreen(
    navController: NavController,
    viewModel: ChatViewModel = hiltViewModel()
    ) {
    val state = viewModel.state.value

    Column(
        modifier = Modifier.fillMaxSize().background(Color.Green)
    ) {

        ScreenName(screenName = "Chat")

        LazyColumn(
            Modifier.fillMaxSize()
                .background(Color.Magenta)
                .padding(5.dp)
        ) {
            items(items = state.messages, key = { it.messageId }) { message ->
                ChatMessageItem(
                    message = message
                )
            }
        }
    }
}