package com.example.chatexu.presentation.chat


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.chatexu.common.Constants
import com.example.chatexu.domain.model.MessageType
import com.example.chatexu.presentation.chat.components.ChatMessageItem
import com.example.chatexu.presentation.commons.composable.ScreenName

@Composable
fun ChatScreen(
    navController: NavController,
    viewModel: ChatViewModel = hiltViewModel()
    ) {
    val state = viewModel.state.value
    var lastMessageSender = Constants.ID_DEFAULT

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Green)
    ) {

        ScreenName(screenName = "Chat")



        LazyColumn(
            Modifier
                .fillMaxSize()
                .background(Color.Magenta)
                .padding(5.dp)
        ) {
            items(items = state.messages, key = { it.messageId }) { message ->

                val verticalPadding =
                    if (lastMessageSender == message.senderId)
                        2.dp
                    else
                        5.dp
                lastMessageSender = message.senderId

                Log.d("msgarr", "ChatMessageItem - uId: ${state.userId}; sId = ${message.senderId}")
                val arrangement =
                    if(message.senderId == state.userId){
                        Log.d("msgarr", "ChatMessageItem - arrangement end")
                        Arrangement.End
                    }
                    else{
//                        Log.d("msgarr", "ChatMessageItem - arrangement start")
                        Arrangement.Start
                    }


                Row(
                    modifier = Modifier.fillMaxWidth().padding(start = 5.dp, end = 5.dp, top = verticalPadding),
                    horizontalArrangement = arrangement,
                ) {
                    ChatMessageItem(
                        message = message,
                        viewerId = state.userId
                    )

                }
            }
        }


    }
}