package com.example.chatexu.presentation.add_friend.components

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.example.chatexu.common.Constants
import com.example.chatexu.domain.model.User
import com.example.chatexu.presentation.commons.composable.UserIcon
import com.example.chatexu.presentation.getUserErrorIcon

@Composable
fun StrangerItem(
    user: User,
    onItemClick: (User) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick(user) }
            .background(Color.LightGray)
            .padding(8.dp)
        , verticalAlignment = Alignment.CenterVertically,
    ) {
        
        val strangerProfileIcon: Bitmap = user.icon ?: getUserErrorIcon(LocalContext.current)

        UserIcon(icon = strangerProfileIcon, modifier = Modifier.padding(8.dp))
//        Image(
//            modifier = Modifier.padding(8.dp),
//            bitmap = friendProfileIcon.asImageBitmap(),
//            contentDescription = "User icon"
//        )

//        Column {
            Text(
                text = user.nickname,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier.padding(start = 8.dp, end = 24.dp)
            )
//        }
    }
}


@Preview(showBackground = true)
@Composable
private fun ItemPreview() {
    val friend = User(
        id = Constants.ID_DEFAULT,
        nickname = "STRANGER USER NICKNAME",
        icon = null,
        username = "STRANGER USER USERNAME"
    )

    StrangerItem(
        user = friend,
        {}
    )
}
