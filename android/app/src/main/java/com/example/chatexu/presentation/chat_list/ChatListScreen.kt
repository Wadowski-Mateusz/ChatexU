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
import com.example.chatexu.presentation.commons.composable.ScreenName

@Composable
fun ChatListScreen(
    navController: NavController,
    viewModel: ChatListViewModel = hiltViewModel()
) {
    val state = viewModel.state.value

    Box() {


        if (state.error.isNotBlank()) {
            Text(
                text = state.error,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            )
        }

        // Chats
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray)
        ) {
//        ChatsListLazy(chatRows = state.chatRows, navController = navController)
            ScreenName(screenName = "Chat List")
            LazyColumn(modifier = Modifier
                .fillMaxWidth()
                .background(Color.Magenta)) {
                items(items = state.chatRows, key = { it.chatId }) { chatRow ->
                    ChatListItem(
                        chatRow = chatRow,
                        onItemClick = {
                            navController.navigate(Screen.ChatScreen.route + "/${state.userId}/${chatRow.chatId}")
                        }
                    )
                }
            }

        }
        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .background(Color.Blue),
            horizontalArrangement = Arrangement.End
        ) {

            // Add friend
            FloatingActionButton(
                modifier = Modifier
                    .padding(8.dp)
                    .wrapContentSize(),
                onClick = {
                    Log.d(DebugConstants.PEEK, "Add friend")
                    navController.navigate(Screen.AddFriendScreen.route + "/${state.userId}")
                }
            ) {
                Icon(Icons.Filled.AddCircle, "Create chat", modifier = Modifier.size(32.dp))
            }


            // Create chat
            FloatingActionButton(
                modifier = Modifier
                    .padding(8.dp)
                    .wrapContentSize(),
                onClick = {
                    Log.d(DebugConstants.PEEK, "Create chat")
                    navController.navigate(Screen.CreateChatScreen.route + "/${state.userId}")
                }
            ) {
                Icon(Icons.Filled.MailOutline, "Create chat", modifier = Modifier.size(32.dp))
            }


            // User Options
            FloatingActionButton(
                modifier = Modifier
                    .padding(8.dp)
                    .wrapContentSize(),
                onClick = {
                    Log.d(DebugConstants.PEEK, "User options")
                    navController.navigate(Screen.UserOptionsScreen.route + "/${state.userId}")
                }
            ) {
                Icon(Icons.Filled.AccountCircle, "User options", modifier = Modifier.size(32.dp))
            }




        }
    }

}
