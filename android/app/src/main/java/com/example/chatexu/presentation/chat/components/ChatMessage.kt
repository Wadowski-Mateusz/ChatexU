package com.example.chatexu.presentation.chat.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ChatMessage(
//    content: Content
) {
    Row(modifier = Modifier.fillMaxSize()) {
        Text(text = "CHAT", modifier = Modifier.fillMaxSize())
    }
}