package com.example.chatexu.presentation.chat.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.chatexu.common.Constants
import com.example.chatexu.common.DebugConstants
import com.example.chatexu.domain.model.Message
import com.example.chatexu.domain.model.MessageType
import java.time.Instant



@Composable
fun MessageLazyList(
    messages: List<Message>,
    userId: String,
) {
    LazyColumn() {
        items(items = messages, key = {it.messageId}) { message ->
            ChatMessageItem(
                message = message,
                viewerId = userId,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatRowPreview() {
    val m = Message(
        messageId = Constants.ID_DEFAULT,
        senderId = Constants.ID_DEFAULT,
        chatId = Constants.ID_DEFAULT,
        messageType = MessageType.Text(DebugConstants.lorem(10)),
        timestamp = Instant.now()
    )

    val m2 = m.copy(
        messageType = MessageType.Text(DebugConstants.lorem(5)),
        timestamp = Instant.now()
    )

    val messages: List<Message> = listOf(m, m2)
    MessageLazyList(
        messages = messages,
        userId = Constants.ID_DEFAULT
    )
}
