package com.example.chatexu.presentation.chat_list.components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import java.time.LocalDate
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
        .background(Color(0xFFDDDDDD))
        .clickable { onItemClick(chatRow) }
        .border(width = 1.dp, color = Color.LightGray)
        .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val icon: Bitmap = chatRow.icon ?: getUserErrorIcon(LocalContext.current)
        Column ( modifier = Modifier.weight(1.0f) ) {
            Image(
                bitmap = icon.asImageBitmap(),
                contentDescription = "User icon"
            )
        }

        // Chat name and last message
        Column(
            modifier = Modifier
                .weight(4.0f)
                .padding(horizontal = 8.dp),
            ) {
            Text(
                text = chatRow.chatName,
                fontWeight = FontWeight.Bold,
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
            val datetime: LocalDateTime  = LocalDateTime.ofInstant(chatRow.timestamp, zoneId)

            val formattedDate = if (datetime.toLocalDate() == LocalDate.now())
                DateTimeFormatter.ofPattern("hh:mm a").format(datetime)
            else if (datetime.toLocalDate().year == LocalDate.now().year)
                DateTimeFormatter.ofPattern("d LLL").format(datetime)
            else
                DateTimeFormatter.ofPattern("d LLL yyyy").format(datetime)
            Text(text = formattedDate)
        }

    }
}

@Preview(showBackground = true)
@Composable
fun ChatRowPreview() {
    val context = LocalContext.current
    val input = context.resources.openRawResource(R.raw.defaulticon)
    val bitmap = BitmapFactory.decodeStream(input)
    input.close()

    val chat = ChatRow(
        chatId = UUID.randomUUID().toString(),
        chatName = "User1",
        lastMessage = MessageType.Text("Hello"),
        timestamp = Instant.now(),
        icon = bitmap
    )
    ChatListItem(chatRow = chat, {})
}

