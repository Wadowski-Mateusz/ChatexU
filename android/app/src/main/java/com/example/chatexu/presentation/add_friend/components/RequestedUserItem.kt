package com.example.chatexu.presentation.add_friend.components

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
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
import com.example.chatexu.common.getUserErrorIcon
import com.example.chatexu.presentation.ui.theme.LightGreen

@Composable
fun RequestedUserItem(
    user: User,
    onItemClick: (User) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick(user) }
            .background(Color(0xFF246424))
            .padding(8.dp)
//        , verticalAlignment = Alignment.CenterVertically,
    ) {
        
        val friendProfileIcon: Bitmap = user.icon ?: getUserErrorIcon(LocalContext.current)

        Row(
            modifier = Modifier
                .align(Alignment.CenterStart),
            verticalAlignment = Alignment.CenterVertically
        ) {

            UserIcon(icon = friendProfileIcon, modifier = Modifier.padding(8.dp))
//            Image(
//                modifier = Modifier.padding(8.dp),
//                bitmap = friendProfileIcon.asImageBitmap(),
//                contentDescription = "User icon"
//            )

            Text(
                text = user.nickname,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier.padding(start = 8.dp, end = 24.dp),
            )
        }

//        Column {

        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = "Request is sended",
            modifier = Modifier.align(Alignment.CenterEnd)
        )
//        }
    }
}


@Preview(showBackground = true)
@Composable
private fun ItemPreview() {
    val friend = User(
        id = Constants.ID_DEFAULT,
        nickname = "USER NICKNAME",
        icon = null,
        username = "USER USERNAME"
    )

    RequestedUserItem(
        user = friend,
        {}
    )
}
