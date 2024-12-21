package com.example.chatexu.presentation.commons.composable


import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun UserIcon(icon: Bitmap, modifier: Modifier = Modifier, size: Dp = 64.dp, contentDescription: String = "User icon") {

    Image(
        modifier = modifier.size(size),
//        contentScale = ContentScale.Fit,
        bitmap = icon.asImageBitmap(),
        contentDescription = contentDescription,
    )
    
}

