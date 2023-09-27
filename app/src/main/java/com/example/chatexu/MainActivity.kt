package com.example.chatexu

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
import androidx.compose.runtime.ComposableOpenTarget
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.chatexu.data.models.ChatRow
import com.example.chatexu.ui.theme.ChatexUTheme
import java.time.Instant
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
                    val testChat = mainVM.getChatRows().collectAsState(initial = emptyList())
//                    Text(text = testChat.value.toString())
                    ChatList(chatRows = testChat.value)
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
        items(items = chatRows, key = {it.id}) { chatRow ->
            ChatRowRow(chatRow = chatRow)

        }
    }

}


@Composable
fun ChatRowRow(chatRow: ChatRow) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(1.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
            ) {
                Column {
                    Text(text = chatRow.chatName, fontWeight = FontWeight.Bold)
                    Text(text = chatRow.message)
                }
            Text(text = chatRow.muted.toString())
        }

    }

}

@Preview(showBackground = true)
@Composable
fun ChatRowPreview() {
    val list = generateSequence{ ChatRow(chatName = "User${Random.nextInt()%10}", message = "Message".repeat(Math.abs(Random.nextInt()%10)), muted = false, from = Instant.now()) }
        .take(50)
        .toList()
    val chat1 = ChatRow(chatName = "User1", message = "Message", muted = false, from = Instant.now())
    val chat2 = ChatRow(chatName = "User2", message = "Message", muted = false, from = Instant.now())
//    ChatRowRow(chatRow = chat1)
//    ChatRowRow(chatRow = chat2)
    ChatListLazyColumn(listOf(chat1, chat2))
    ChatListLazyColumn(list)
//    ChatRowRow(chatRow = ChatRow(chatName = "User2", message = "Message", muted = false, from = Instant.now()))
}