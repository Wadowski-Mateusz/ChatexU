package com.example.chatexu.presentation.chat.components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.example.chatexu.common.Constants
import com.example.chatexu.common.DebugConstants
import com.example.chatexu.domain.model.Message
import com.example.chatexu.domain.model.MessageType
import android.util.Base64
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.ui.graphics.ImageBitmap

// TODO long messages

@Composable
fun ChatMessageItem(
//    content: Content
    message: Message,
    viewerId: String,
) {


    when(message.messageType) {
            // TODO messages types
            is MessageType.Text -> {
                Text(
                    text = message.messageType.text,
                    color = Color.White,
                    softWrap = true,
                    modifier = Modifier
                        .background(
                            color = Color.Blue,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 20.dp, vertical = 10.dp)
                )
            }
            is MessageType.Resource -> {
                Log.w(DebugConstants.TODO, "ChatMessageItem() - resource")

                Image(
//                    modifier = Modifier.size(200.dp, 150.dp),
//                    contentScale =
                    modifier = Modifier
                        .width(320.dp)
                        .height(180.dp)
                        .clickable { Log.w(DebugConstants.TODO, "MAKE IMAGE FULLSCREEN") },
                    contentScale = ContentScale.Fit,
                    bitmap = base64ToBitmap(message.messageType.uri),
                    contentDescription = "Message image",
                )

            }
            is MessageType.Deleted -> {
                Log.w(DebugConstants.TODO, "ChatMessageItem() - deleted")
                Text(
                    text = "ChatMessageItem() - deleted",
                    color = Color.White,
                    softWrap = true,
                    modifier = Modifier
                        .background(color = Color.Blue, shape = RoundedCornerShape(8.dp))
                        .padding(horizontal = 20.dp, vertical = 10.dp)
                )
            }
            is MessageType.Initialization -> {
                Log.w(DebugConstants.TODO, "ChatMessageItem() - init")
                Text(
                    text = "ChatMessageItem() - init",
                    color = Color.White,
                    softWrap = true,
                    modifier = Modifier
                        .background(color = Color.Blue, shape = RoundedCornerShape(8.dp))
                        .padding(horizontal = 20.dp, vertical = 10.dp)
                )
            }
        }
}

fun base64ToBitmap(base64: String): ImageBitmap {
    val bytes: ByteArray = Base64.decode(base64, Base64.DEFAULT)
    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    return bitmap.asImageBitmap()
}




//@Preview
//@Composable
//fun PreviewChatMessage() {
////    ChatMessageItem(testStr = DebugConsts.lorem(10))
////    ChatMessageItem(testStr = DebugConsts.lorem(100))
//    ChatMessageItem(testStr = DebugConsts.lorem(1000))
//}