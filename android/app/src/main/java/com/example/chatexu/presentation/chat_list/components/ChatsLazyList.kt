package com.example.chatexu.presentation.chat_list.components

import android.graphics.BitmapFactory
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.chatexu.R
import com.example.chatexu.domain.model.ChatRow
import com.example.chatexu.domain.model.MessageType
import com.example.chatexu.presentation.Screen
import com.example.chatexu.presentation.chat_list.ChatListViewModel
import java.time.Instant
import java.util.UUID
import kotlin.math.abs
import kotlin.random.Random

@Composable
fun ChatsListLazy(
    chatRows: List<ChatRow>,
    navController: NavController,
    userId: String,
    jwt: String,

) {
    LazyColumn() {
        items(items = chatRows, key = {it.chatId}) { chatRow ->
            ChatListItem(
                chatRow = chatRow,
                onItemClick = {
                    navController.navigate(
                        Screen.ChatScreen.route
                                + "/${userId}"
                                + "/${jwt}"
                                + "/${chatRow.chatId}"
                    )
                }
            )
        }
    }

//    if (state.isLoading) {
//        CircularProgressIndicator()
//    }
}



@Preview(showBackground = true)
@Composable
fun ChatsListLazyPreview() {
    val context = LocalContext.current
    val input = context.resources.openRawResource(R.raw.green)
    val bitmap = BitmapFactory.decodeStream(input)
    input.close()



    val list = generateSequence{
        ChatRow(
            chatId = UUID.randomUUID().toString(),
            chatName = "User${abs(Random.nextInt()) % 10 + 1}",
            lastMessage =
            MessageType.Text("Message".repeat(abs(Random.nextInt() % 100 + 1))),
            timestamp = Instant.now(),
            icon = bitmap
        )
    }
        .take(10)
        .toList()
    ChatsListLazy(list, NavController(LocalContext.current), userId = "", jwt = "")
}
