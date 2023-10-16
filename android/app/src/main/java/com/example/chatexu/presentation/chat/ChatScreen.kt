package com.example.chatexu.presentation.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.chatexu.presentation.chat.components.MessageLazyList
import com.example.chatexu.presentation.commons.composable.ScreenName
import kotlinx.coroutines.launch
import kotlin.math.max


@Composable
fun ChatScreen(
    navController: NavController,
    viewModel: ChatViewModel = hiltViewModel()
    ) {
    
    val state = viewModel.state.value

    val inputMessage = remember { mutableStateOf(TextFieldValue()) }

//    val scope = rememberCoroutineScope()
//    val listState = rememberLazyListState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Green)
    ) {

        Row(verticalAlignment = Alignment.Top) { ScreenName(screenName = "Chat") }



        MessageLazyList(
            messages = state.messages, userId = state.userId,
//            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Magenta)
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
//                    .fillMaxWidth()
                        ,
                value = inputMessage.value,
                onValueChange = { inputMessage.value = it },
                placeholder = { Text(text = "Enter your message") },
                maxLines = 3
            )


            Icon(
                Icons.Rounded.ArrowForward,
//                contentDescription = stringResource(id = R.string.shopping_cart_content_desc)
                contentDescription = "Send message",
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 16.dp, start = 8.dp, end = 16.dp)
//                    .weight(1f)
//                    .clickable {  }
            )
        }

    }

}
