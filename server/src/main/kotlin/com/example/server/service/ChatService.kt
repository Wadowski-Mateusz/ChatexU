package com.example.server.service

import com.example.server.repository.ChatRepository
import org.springframework.stereotype.Service

@Service
class ChatService(private val chatRepository: ChatRepository) {
}