package com.example.chatexu

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.chatexu.data.models.ChatRow
import com.example.chatexu.ui.theme.ChatexUTheme
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.UUID
import kotlin.random.Random


class MainActivity : ComponentActivity() {

    private val mainVM by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChatexUTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    val testChat = mainVM.getChatRows().collectAsState(initial = emptyList())
//                    ChatList(chatRows = testChat.value)

                    mainVM.getChatRows(UUID.randomUUID(), UUID.randomUUID(), context = applicationContext)
                    val rows = mainVM.chatRowList.collectAsState(initial = emptyList())
                    ChatList(chatRows = rows.value)
                }

            }
        }
    }
}

@Composable
fun ChatList(chatRows: List<ChatRow>) {
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        ChatListLazyColumn(chatRows)
    }
}


@Composable
fun ChatListLazyColumn(chatRows: List<ChatRow>) {
    LazyColumn() {
        items(items = chatRows, key = {it.chatId}) { chatRow ->
            ChatRowRow(chatRow = chatRow)

        }
    }

}


@Composable
fun ChatRowRow(chatRow: ChatRow) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
//            .clickable {  }
            .padding(1.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.SpaceAround
        ) {
            // Chat icon
            Column (
//                modifier = Modifier.weight(1.0f)
            ) {
                Image(
                    bitmap = chatRow.icon!!.asImageBitmap(),
                    contentDescription = "User icon",
                )
            }

            // Chat name and last message
            Column(
                modifier = Modifier
                //                .weight(4.0f)
                    .padding(horizontal = 8.dp),
//                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top,
            ) {
                Text(text = chatRow.chatName, fontWeight = FontWeight.Bold)
                Text(text = chatRow.lastMessage)
            }

            // Time of last activity
            Column(
//                modifier = Modifier.
            //                weight(2f)
                horizontalAlignment = Alignment.End
            ) {

                val zoneId = ZoneId.systemDefault()
                val datetime  = LocalDateTime.ofInstant(chatRow.timestamp, zoneId)
                val formatted = DateTimeFormatter.ofPattern("d LLL yyyy hh:mm:ss").format(datetime)
                Text(text = formatted)
            }

            // TODO
            // is chat muted icon
        }

    }

}

//@Preview(showBackground = true)
//@Composable
//fun ChatRowPreview() {
//    val context = LocalContext.current
//    val input = context.resources.openRawResource(R.raw.green)
//    val bitmap = BitmapFactory.decodeStream(input)
//    input.close()
//
//    val chat1 = ChatRow(chatId = UUID.randomUUID(), chatName = "User1", lastMessage = "Message", timestamp = Instant.now(), icon = bitmap)
//    ChatRowRow(chatRow = chat1)
////    val chat2 = ChatRow(chatId = UUID.randomUUID(), chatName = "User2", lastMessage = "Message", timestamp = Instant.now())
////    ChatRowRow(chatRow = chat2)
//
////    ChatListLazyColumn(listOf(chat1, chat2))
////
////    val list = generateSequence{
////        ChatRow(
////            chatId = UUID.randomUUID(),
////            chatName = "User${Random.nextInt() % 10 + 1}",
////            lastMessage = "Message".repeat(Math.abs(Random.nextInt() % 10 + 1)),
////            timestamp = Instant.now(),
////            icon = bitmap
////        )
////    }
////        .take(32)
////        .toList()
////    ChatListLazyColumn(list)
//}







// ROOM
//@Composable
//fun ChatList(chatRows: List<ChatRow>) {
//    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
//        ChatListLazyColumn(chatRows)
//    }
//}
//
//
//@Composable
//fun ChatListLazyColumn(chatRows: List<ChatRow>) {
//    LazyColumn() {
//        items(items = chatRows, key = {it.id}) { chatRow ->
//            ChatRowRow(chatRow = chatRow)
//
//        }
//    }
//
//}
//
//
//@Composable
//fun ChatRowRow(chatRow: ChatRow) {
//    Surface(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(1.dp),
//        shape = RoundedCornerShape(10.dp)
//    ) {
//        Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.SpaceAround
//            ) {
//                Column {
//                    Text(text = chatRow.chatName, fontWeight = FontWeight.Bold)
//                    Text(text = chatRow.message)
//                }
//            Text(text = chatRow.muted.toString())
//        }
//
//    }
//
//}
