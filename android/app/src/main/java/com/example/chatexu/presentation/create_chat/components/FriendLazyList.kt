package com.example.chatexu.presentation.create_chat.components


import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.chatexu.domain.model.Friend
import com.example.chatexu.presentation.Screen
import com.example.chatexu.presentation.create_chat.CreateChatState
import com.example.chatexu.presentation.create_chat.CreateChatViewModel
import org.mongodb.kbson.BsonObjectId
import kotlin.random.Random

@Composable
fun FriendLazyList(
    friends: List<Friend>,
    navController: NavController,
    userId: String,
    getChatOrElseCreate: (String, String) -> String,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(items = friends, key = {it.id}) {friend ->
            FriendItem(
                friend = friend,
                onItemClick = {
                    Log.d("PEEK", "Friend ${friend.nickname} clicked for user $userId.")
                    val chatId = getChatOrElseCreate(userId, friend.id)
                    Log.d("PEEK", "item - Returned id: $chatId")
                    navController.navigate(Screen.ChatScreen.route + "/${userId}" + "/${chatId}")
                }
            )
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//private fun FriendLazyListPreview() {
//    val friend = Friend(
//        id = BsonObjectId().toString(),
//        nickname = "Nickname nickname",
//        nicknameFromChat = "Chat Nickname",
//        icon = null
//    )
//
//    FriendLazyList(
//        friends = generateSequence {
//            friend.copy(
//                id = BsonObjectId().toString(),
//                nicknameFromChat =
//                if (Random.nextInt() < 0) friend.nicknameFromChat
//                else ""
//            )
//        }
//            .take(12)
//            .toList()
//        , NavController(LocalContext.current)
//        , ""
//        , { a: String, b: String ->  }
//
//    )
//}
