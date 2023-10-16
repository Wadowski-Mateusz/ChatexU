package com.example.chatexu.presentation.chat.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.chatexu.common.Constants
import com.example.chatexu.domain.model.Message
import kotlinx.coroutines.launch
import kotlin.math.max


@Composable
fun MessageLazyList(
    messages: List<Message>,
    userId: String,
    modifier: Modifier? = null,
//    state: LazyListState
) {
        var lastMessageSender = Constants.ID_DEFAULT


        LazyColumn(
//            state = state,
            modifier = modifier ?:
                Modifier
                    .fillMaxSize()
                    .background(Color.Magenta)
                    .padding(5.dp)
        ) {
            items(items = messages, key = { it.messageId }) { message ->

                val verticalPadding =
                    if (lastMessageSender == message.senderId)
                        2.dp
                    else
                        5.dp
                lastMessageSender = message.senderId

                Log.d("msgarr", "ChatMessageItem - uId: $userId; sId = ${message.senderId}")
                val arrangement =
                    if(message.senderId == userId){
                        Log.d("msgarr", "ChatMessageItem - arrangement end")
                        Arrangement.End
                    }
                    else{
//                        Log.d("msgarr", "ChatMessageItem - arrangement start")
                        Arrangement.Start
                    }


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

