package com.example.chatexu.presentation.chat.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.chatexu.common.DebugConsts
import com.example.chatexu.domain.model.Message
import com.example.chatexu.domain.model.MessageType
import org.mongodb.kbson.BsonObjectId
import org.mongodb.kbson.ObjectId
import java.time.Instant



@Composable
fun MessageLazyList(
    messages: List<Message>,
) {
    LazyColumn() {
        items(items = messages, key = {it.messageId}) { message ->
            ChatMessageItem(testStr =
            when(message.messageType) {
                is MessageType.Text -> {
                    message.messageType.text
                }
                is MessageType.Initialization -> {
                    "No messages yet."
                }
                else -> {
                    throw NotImplementedError("MessageLazyList")
                }
            }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatRowPreview() {
    val m = Message(
        messageId = ObjectId(),
        senderId = ObjectId(),
        chatId = ObjectId(),
        messageType = MessageType.Text(DebugConsts.lorem(10)),
        timestamp = Instant.now()
    )
    val m2 = Message(
        messageId = ObjectId(),
        senderId = ObjectId(),
        chatId = ObjectId(),
        messageType = MessageType.Text(DebugConsts.lorem(5)),
        timestamp = Instant.now()
    )

    val messages: List<Message> = listOf(m, m2)
    MessageLazyList(messages = messages)

}
