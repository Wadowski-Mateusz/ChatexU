package com.example.chatexu.data.models

import androidx.annotation.NonNull
import androidx.room.BuiltInTypeConverters
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant
import java.time.LocalDateTime
import java.util.UUID

@Entity(tableName = "chat_rows")
data class ChatRow(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    val chatName: String,
    val message: String,
    val from: Instant,
    val muted: Boolean,
)