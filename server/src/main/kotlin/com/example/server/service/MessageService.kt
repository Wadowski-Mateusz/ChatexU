package com.example.server.service

import com.example.server.repository.MessageRepository
import org.springframework.stereotype.Service

@Service
class MessageService(private val messageRepository: MessageRepository) {
}