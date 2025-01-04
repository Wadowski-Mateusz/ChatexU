package com.example.chatexu.presentation.chat_list

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.chatexu.common.DebugConstants
import com.example.chatexu.presentation.Screen
import com.example.chatexu.presentation.chat_list.components.ChatListItem

@Composable
fun ChatListScreen(
    navController: NavController,
    viewModel: ChatListViewModel = hiltViewModel()
) {
    val state = viewModel.state.value

    Box {
        if (state.error.isNotBlank()) {
            Text(
                text = state.error,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }

        // List of chats
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray)
        ) {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(items = state.chatRows, key = { it.chatId }) { chatRow ->
                    ChatListItem(
                        chatRow = chatRow,
                        onItemClick = {
                            navController.navigate(
                                Screen.ChatScreen.route
                                        + "/${state.userId}"
                                        + "/${state.jwt}"
                                        + "/${chatRow.chatId}") }) } } }

        // Buttons
        Row(
            modifier = Modifier.align(Alignment.BottomEnd),
            horizontalArrangement = Arrangement.End
        ) {

            val floatingActionButtonModifier: Modifier = Modifier.padding(8.dp).wrapContentSize()
            val floatingActionButtonIconModifier: Modifier = Modifier.size(32.dp)

            // Add friend
            FloatingActionButton(
                modifier = floatingActionButtonModifier,
                onClick = {
                    navController.navigate(
                        Screen.AddFriendScreen.route
                                + "/${state.userId}"
                                + "/${state.jwt}"
                    )
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.AddCircle,
                    contentDescription = "Add friend",
                    modifier = floatingActionButtonIconModifier
                )
            }

            // Create chat
            FloatingActionButton(
                modifier = floatingActionButtonModifier,
                onClick = {
                    Log.d(DebugConstants.PEEK, "Create chat")
                    navController.navigate(
                        Screen.CreateChatScreen.route
                                + "/${state.userId}"
                                + "/${state.jwt}"
                    )
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.MailOutline,
                    contentDescription = "Create chat",
                    modifier = floatingActionButtonIconModifier
                )
            }

            // User Options
            FloatingActionButton(
                modifier = floatingActionButtonModifier,
                onClick = {
                    navController.navigate(
                        Screen.UserOptionsScreen.route
                                + "/${state.userId}"
                                + "/${state.jwt}"
                    )
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "User options",
                    modifier = floatingActionButtonIconModifier
                )
            }
        }

    }
}
