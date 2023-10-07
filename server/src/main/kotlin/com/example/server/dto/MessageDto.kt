package com.example.server.dto

import com.example.server.model.MessageContent
import com.example.server.model.Message
import org.bson.types.ObjectId
import java.time.Instant


data class MessageDto(
    val messageId: String,
    val senderId: String,
    val chatId: String,
    val timestamp: Instant,
    val messageContent: MessageContent //TODO data class contentDto()
) {

    // Jackson needs either a no-argument constructor (default constructor) or properly annotated properties.
//    constructor() : this("", "", "", Instant.now(), Content.TextContent(""))



    fun toMessage(): Message {
        return when (messageContent) {
            is MessageContent.Text -> {
               Message.Builder()
                   .messageId(ObjectId(messageId))
                   .senderId(ObjectId(senderId))
                   .chatId(ObjectId(chatId))
                   .timestamp(timestamp)
                   .messageContent(messageContent) // TODO
                   .build()
            }

            is MessageContent.Resource -> TODO()
//            is Content.MixedContent -> TODO()
        }

    }
}
fun Message.toDto(): MessageDto {
    return MessageDto(
        messageId.toHexString(),
        senderId.toHexString(),
        chatId.toHexString(),
        timestamp,
        messageContent
        )
}
