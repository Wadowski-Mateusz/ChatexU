package com.example.chatexu.presentation.create_chat.components


import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.chatexu.domain.model.Friend
import com.example.chatexu.presentation.Screen

@Composable
fun FriendLazyList(
    friends: List<Friend>,
    navController: NavController,
    userId: String,
    jwt: String,
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
//                    Log.d("PEEK", "Friend ${friend.nickname} clicked for user $userId.")
                    val chatId = getChatOrElseCreate(userId, friend.id)
//                    Log.d("PEEK", "item - Returned id: $chatId")
                    navController.navigate(Screen.ChatScreen.route
                            + "/${userId}"
                            + "/${jwt}"
                            + "/${chatId}"
                    )
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
