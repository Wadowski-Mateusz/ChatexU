package com.example.chatexu.presentation.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.chatexu.common.DebugConsts
import com.example.chatexu.domain.model.Message

// TODO long messages

@Composable
fun ChatMessageItem(
//    content: Content
    message: Message
) {

    val rounder = 25

    Row(
        Modifier.padding(5.dp)
    ) {
        Text(
            text = "Message test",
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

//@Preview
//@Composable
//fun PreviewChatMessage() {
////    ChatMessageItem(testStr = DebugConsts.lorem(10))
////    ChatMessageItem(testStr = DebugConsts.lorem(100))
//    ChatMessageItem(testStr = DebugConsts.lorem(1000))
//}