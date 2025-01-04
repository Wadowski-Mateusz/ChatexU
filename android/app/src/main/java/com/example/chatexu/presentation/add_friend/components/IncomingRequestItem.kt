package com.example.chatexu.presentation.add_friend.components

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatexu.common.Constants
import com.example.chatexu.common.DebugConstants
import com.example.chatexu.domain.model.User
import com.example.chatexu.presentation.commons.composable.UserIcon
import com.example.chatexu.common.getUserErrorIcon
import com.example.chatexu.presentation.ui.theme.LightGreen


@Composable
fun IncomingRequestItem(
    user: User,
    onItemClickAccept: (User) -> Unit,
    onItemClickReject: (User) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFA4F4A4))
            .padding(8.dp)
    ) {
        val iconSize = 48.dp
        val iconPadding = 4.dp
        val friendProfileIcon: Bitmap = user.icon ?: getUserErrorIcon(LocalContext.current)

        Row(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(end = 16.dp)
            , verticalAlignment = Alignment.CenterVertically
        ) {

            UserIcon(icon = friendProfileIcon, modifier = Modifier.padding(8.dp))

            Text(
                text = user.nickname
                , fontWeight = FontWeight.Bold
                , fontSize = 24.sp
                , overflow = TextOverflow.Ellipsis
                , maxLines = 1
                , modifier = Modifier.padding(start = 8.dp)
            )
        }

        Row(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxWidth()
            , horizontalArrangement = Arrangement.End
        ) {

            Icon( // Accept
                imageVector = Icons.Default.Check
                , contentDescription = "Accept request"
                , modifier = Modifier.size(iconSize)
                    .padding(iconPadding)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF44CC44))
                    .clickable { onItemClickAccept(user) }
            )

            Icon( // Reject
                imageVector = Icons.Default.Close
                , contentDescription = "Reject request"
                , modifier = Modifier.size(iconSize)
                    .padding(iconPadding)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFCC4444))
                    .clickable { onItemClickReject(user) }
            )
        }
    }
}


//@Composable
//fun IncomingRequestItem(
//    user: User,
//    onItemClick: (User) -> Unit
//) {
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clickable { onItemClick(user) }
//            .background(LightGreen)
//            .padding(8.dp)
//    ) {
//        val iconSize = 36.dp
//        val iconPadding = 4.dp
//        val friendProfileIcon: Bitmap = user.icon ?: getUserErrorIcon(LocalContext.current)
//
//        Row(
//            modifier = Modifier
//                .align(Alignment.CenterStart)
//                .padding(end = iconSize * 2)
//            , verticalAlignment = Alignment.CenterVertically
//
//        ) {
//            Image(
//                modifier = Modifier
//                    .padding(start = 8.dp)
//                    .size(iconSize),
//                bitmap = friendProfileIcon.asImageBitmap(),
//                contentDescription = "User icon"
//            )
//
//            Text(
//                text = user.nickname,
//                fontWeight = FontWeight.Bold,
//                fontSize = 24.sp,
//                overflow = TextOverflow.Ellipsis,
//                maxLines = 1,
//                modifier = Modifier.padding(start = 8.dp)
//            )
//        }
//
//        Row(
//            modifier = Modifier
//                .align(Alignment.CenterEnd)
//                .background(Color.Blue),
//            horizontalArrangement = Arrangement.End
//        ) {
//            Icon(
//                imageVector = Icons.Default.Check,
//                contentDescription = "Accept request",
//                modifier = Modifier.size(iconSize).padding(iconPadding),
//            )
//
//            Icon(
//                imageVector = Icons.Default.Close,
//                contentDescription = "Reject request",
//                modifier = Modifier.size(iconSize).padding(iconPadding),
//            )
//        }
//    }
//}


@Preview(showBackground = true)
@Composable
private fun ItemPreview() {
    val friend = User(
        id = Constants.ID_DEFAULT,
//        nickname = "USER NICKNAME NICKNAME",
        nickname = DebugConstants.loremChars(16),
        icon = null,
        username = "USER USERNAME"
    )

    IncomingRequestItem(
        user = friend,
        {}, {}
    )
}
