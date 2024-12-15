package com.example.chatexu.presentation.create_chat.components

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatexu.domain.model.Friend
import com.example.chatexu.presentation.commons.composable.UserIcon
import com.example.chatexu.common.getUserErrorIcon
import org.mongodb.kbson.ObjectId

@Composable
fun FriendItem(
    friend: Friend,
    onItemClick: (Friend) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick(friend) }
            .background(Color.LightGray)
            .padding(8.dp)
        , verticalAlignment = Alignment.CenterVertically,
    ) {
        
        val friendProfileIcon: Bitmap = friend.icon ?: getUserErrorIcon(LocalContext.current)


        UserIcon(
            modifier = Modifier.padding(8.dp),
            icon = friendProfileIcon,
            contentDescription = "Friend icon",
            size = 64.dp
        )

        Column {
            Text(
                text = friend.nickname,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            if(friend.nicknameFromChat.isNotBlank())
                Text(
                    text = friend.nicknameFromChat,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun FriendPreview() {
    val friend = Friend(
        id = ObjectId().toString(),
        nickname = "USER NICKNAME",
        nicknameFromChat = "CHAT NICKNAME",
        icon = null
    )

    FriendItem(
        friend = friend,
        {}
    )
}
