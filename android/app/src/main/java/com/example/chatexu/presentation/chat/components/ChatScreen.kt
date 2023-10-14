package com.example.chatexu.presentation.chat.components


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.chatexu.common.DebugConsts
import com.example.chatexu.domain.model.Message
import java.time.Instant

val m = Message(
    messageId = "123",
    senderId = "321",
    content = DebugConsts.lorem(10),
    timestamp = Instant.now()
)
val m2 = Message(
    messageId = "123",
    senderId = "321",
    content = DebugConsts.lorem(5),
    timestamp = Instant.now()
)

val messages2: List<Message> = listOf(m, m2)

@Composable
fun ChatScreen(
    navController: NavController,
//    viewModel

    ) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn() {
            items(items = messages2, key = {it.messageId}) { message ->
                ChatMessageItem(testStr = message.content)
            }
        }
    }

} 