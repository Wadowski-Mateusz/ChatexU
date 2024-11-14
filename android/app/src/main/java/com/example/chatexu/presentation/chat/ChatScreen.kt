package com.example.chatexu.presentation.chat

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.chatexu.common.Constants
import com.example.chatexu.common.DebugConstants
import com.example.chatexu.domain.model.Message
import com.example.chatexu.domain.model.MessageType
import com.example.chatexu.presentation.chat.components.MessageLazyList
import com.example.chatexu.presentation.commons.composable.ScreenName
import java.time.Instant


@Composable
fun ChatScreen(
    navController: NavController,
    viewModel: ChatViewModel = hiltViewModel()
    ) {
    
    val state = viewModel.state.value

    val inputMessage = remember { mutableStateOf(TextFieldValue()) }

    val sendMessage = { answear: String ->
        Log.d(DebugConstants.TODO, "ChatScreen - Message type + message answear for sending")
        val m = Message(
            messageId = Constants.ID_DEFAULT, // TODO id
            senderId = state.userId,
            chatId = state.chatId,
            timestamp = Instant.MIN,
            messageType = MessageType.Text(inputMessage.value.text), //
            isEdited = false,
            replyTo = answear
        )
        viewModel.sendMessage(message = m)
        inputMessage.value = TextFieldValue() // clear input
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray)
    ) {

        Row(verticalAlignment = Alignment.Top) { ScreenName(screenName = "Chat") }


        MessageLazyList(
            messages = state.messages, userId = state.userId,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray)
                .padding(5.dp)
                .weight(1f)
        )


        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 16.dp, start = 16.dp, end = 8.dp)
                    .weight(1f)
                ,
                value = inputMessage.value,
                onValueChange = { inputMessage.value = it },
                placeholder = { Text(text = "Enter your message") },
                maxLines = 3
            )


            Icon(
                Icons.AutoMirrored.Rounded.ArrowForward,
                contentDescription = "Send message",
                modifier = Modifier
                    .clickable {
                        Log.d(DebugConstants.PEEK, "button has been pressed")
                        sendMessage(Constants.ID_DEFAULT)
                    }
                    .padding(top = 16.dp, bottom = 16.dp, start = 8.dp, end = 16.dp)
            )
        }

    }

}