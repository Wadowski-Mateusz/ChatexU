package com.example.chatexu.presentation.chat.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.chatexu.common.DebugConsts
import com.example.chatexu.domain.model.Message
import java.time.Instant



@Composable
fun MessageLazyList(
    messages: List<Message>,
) {
    LazyColumn() {
        items(items = messages, key = {it.messageId}) { message ->
            ChatMessageItem(testStr = message.content)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatRowPreview() {
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

    val messages: List<Message> = listOf(m, m2)
    MessageLazyList(messages = messages)

}
