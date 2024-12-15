package com.example.chatexu.presentation.auth.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun FastButton(txt: String, onClick: () -> Unit) {

    Button(
        modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp, horizontal = 128.dp)
        .background(Color(red = 10, green = 200, blue = 50, alpha = 0)),
        onClick = { onClick() }
    ) {
        Text(txt)
    }
}
