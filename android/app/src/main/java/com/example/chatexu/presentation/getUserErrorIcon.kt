package com.example.chatexu.presentation

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.chatexu.R

fun getUserErrorIcon(context: Context): Bitmap {
    val inputStream = context.resources.openRawResource(R.raw.error)
    val bitmap: Bitmap = BitmapFactory.decodeStream(inputStream)
    inputStream.close()
    return bitmap
}