package com.example.chatexu.presentation.chat_list.components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.chatexu.R
import com.example.chatexu.domain.model.ChatRow
import com.example.chatexu.domain.model.MessageType
import com.example.chatexu.common.getUserErrorIcon
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.UUID

@Composable
fun ChatListItem(
    chatRow: ChatRow,
    onItemClick: (ChatRow) -> Unit
) {
    Row(modifier = Modifier
        .clickable { onItemClick(chatRow) }
        .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.SpaceAround
    ) {


        // Chat icon
//        val context = LocalContext.current
//        val icon: Bitmap = chatRow.icon ?: run {
//            val inputStream = context.resources.openRawResource(R.raw.error)
//            val bitmap: Bitmap = BitmapFactory.decodeStream(inputStream)
//            inputStream.close()
//            bitmap
//        }
        val icon: Bitmap = chatRow.icon ?: getUserErrorIcon(LocalContext.current)

        Column (
            modifier = Modifier.weight(1.0f)
        ) {
            Image(
                bitmap = icon.asImageBitmap(),
                contentDescription = "User icon",)
        }

        // Chat name and last message
        Column(
            modifier = Modifier
                .weight(4.0f)
                .padding(horizontal = 8.dp),
//            horizontalAlignment = Alignment.Start,
//            verticalArrangement = Arrangement.Top,
            ) {
            Text(
                text = chatRow.chatName,
                fontWeight = FontWeight.Bold,
//                    , modifier = Modifier.align(Alignment.Start)
            )
            Text(
                text = when (chatRow.lastMessage) {
                    is MessageType.Text -> chatRow.lastMessage.text
                    is MessageType.Resource -> "Image"
                    is MessageType.Initialization -> "No messages yet."
                    else -> "Message type not implemented - ChatListItem"
                },
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        // Time of last activity
        Column {
            val zoneId = ZoneId.systemDefault()
            val datetime  = LocalDateTime.ofInstant(chatRow.timestamp, zoneId)
            val formatted = DateTimeFormatter.ofPattern("d LLL yyyy hh:mm:ss").format(datetime)
            Text(text = formatted)
        }
        // TODO
        // is chat muted icon


    }
}

@Preview(showBackground = true)
@Composable
fun ChatRowPreview() {
    val context = LocalContext.current
    val input = context.resources.openRawResource(R.raw.green)
    val bitmap = BitmapFactory.decodeStream(input)
    input.close()

    val chat1 = ChatRow(
        chatId = UUID.randomUUID().toString(),
        chatName = "User1",
        lastMessage = MessageType.Text("Message"),
        timestamp = Instant.now(),
        icon = bitmap
    )

    ChatListItem(chatRow = chat1, {})
}

