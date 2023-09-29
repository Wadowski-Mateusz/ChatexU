package com.example.server.controller

import com.example.server.repository.MessageRepository
import com.example.server.service.MessageService
import lombok.AllArgsConstructor
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/message")
@AllArgsConstructor
class MessageController(private val messageService: MessageService) {
}