package com.example.chatexu.presentation.chat.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.chatexu.common.Constants
import com.example.chatexu.common.DebugConstants
import com.example.chatexu.domain.model.Message
import com.example.chatexu.domain.model.MessageType

// TODO long messages

@Composable
fun ChatMessageItem(
//    content: Content
    message: Message,
    viewerId: String,
) {

    val rounder = 25

    when(message.messageType) {
            // TODO messages types
            is MessageType.Text -> {
                Text(
                    text = message.messageType.text,
                    color = Color.White,
                    softWrap = true,
                    modifier = Modifier
                        .background(color = Color.Blue, shape = RoundedCornerShape(rounder))
                        .padding(horizontal = 20.dp, vertical = 10.dp)
//                .padding(10.dp)
//                .fillMaxSize(),
                )
            }
            is MessageType.Resource -> {
                Log.d(DebugConstants.TODO, "ChatMessageItem() - resource")
                Text(
                    text = "ChatMessageItem() - resource",
                    color = Color.White,
                    softWrap = true,
                    modifier = Modifier
                        .background(color = Color.Blue, shape = RoundedCornerShape(rounder))
                        .padding(horizontal = 20.dp, vertical = 10.dp)
//                .padding(10.dp)
//                .fillMaxSize(),
                )
            }
            is MessageType.Deleted -> {
                Log.d(DebugConstants.TODO, "ChatMessageItem() - deleted")
                Text(
                    text = "ChatMessageItem() - deleted",
                    color = Color.White,
                    softWrap = true,
                    modifier = Modifier
                        .background(color = Color.Blue, shape = RoundedCornerShape(rounder))
                        .padding(horizontal = 20.dp, vertical = 10.dp)
//                .padding(10.dp)
//                .fillMaxSize(),
                )
            }
            is MessageType.Initialization -> {
                Log.d(DebugConstants.TODO, "ChatMessageItem() - init")
                Text(
                    text = "ChatMessageItem() - init",
                    color = Color.White,
                    softWrap = true,
                    modifier = Modifier
                        .background(color = Color.Blue, shape = RoundedCornerShape(rounder))
                        .padding(horizontal = 20.dp, vertical = 10.dp)
//                .padding(10.dp)
//                .fillMaxSize(),
                )
            }
        }
}






//@Preview
//@Composable
//fun PreviewChatMessage() {
////    ChatMessageItem(testStr = DebugConsts.lorem(10))
////    ChatMessageItem(testStr = DebugConsts.lorem(100))
//    ChatMessageItem(testStr = DebugConsts.lorem(1000))
//}