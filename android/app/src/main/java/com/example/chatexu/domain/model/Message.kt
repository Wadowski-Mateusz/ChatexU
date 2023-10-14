package com.example.chatexu.domain.model

import android.util.Log
import com.example.chatexu.common.DebugConsts
import java.time.Instant

// Test message
data class Message(
    val messageId: String,
    val senderId: String,
    val content: String,
    val timestamp: Instant,
) {
    init {
        Log.d(DebugConsts.TODO, "implement message class")
    }
}