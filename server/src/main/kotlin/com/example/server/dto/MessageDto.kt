package com.example.server.dto

import com.example.server.model.Content
import java.util.*


data class TextMessageDto(
    val messageId: UUID,
    val sender: UUID,
    val timestamp: UUID,
    val contentDto: Content
)
