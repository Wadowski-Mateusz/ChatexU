package com.example.chatexu.presentation.chat


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun ChatScreen(
    navController: NavController,
//    viewModel

    ) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "DebugText", modifier = Modifier.fillMaxSize())
//        LazyColumn() {
//            items(items = messages, key = {it.messageId}) { message ->
//                ChatMessageItem(testStr = message.content)
//            }
        }
    }
