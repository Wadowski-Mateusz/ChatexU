package com.example.chatexu.presentation.chat.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.chatexu.common.Constants
import com.example.chatexu.domain.model.Message


@Composable
fun MessageLazyList(
    messages: List<Message>,
    userId: String,
    modifier: Modifier = Modifier,
//    state: LazyListState
) {
        var lastMessageSender = Constants.ID_DEFAULT


        LazyColumn(
            modifier = modifier
        ) {
            items(items = messages, key = { it.messageId }) { message ->

                val verticalPadding =
                    if (lastMessageSender == message.senderId)
                        2.dp
                    else
                        5.dp
                lastMessageSender = message.senderId

//                Log.d("msgarr", "ChatMessageItem - uId: $userId; sId = ${message.senderId}")
                val arrangement =
                    if(message.senderId == userId)
                        Arrangement.End
                    else Arrangement.Start



                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 5.dp, end = 5.dp, top = verticalPadding),
                    horizontalArrangement = arrangement,
                ) {
                    ChatMessageItem(
                        message = message,
                        viewerId = userId
                    )

                }
            }
        }

}

