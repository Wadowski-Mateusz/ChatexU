package com.example.server.controller

import com.example.server.dto.ChatView
import com.example.server.model.Chat
import com.example.server.repository.ChatRepository
import com.example.server.service.ChatService
import lombok.AllArgsConstructor
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Instant
import java.util.*

@RestController
@RequestMapping("/chat")
@AllArgsConstructor
class ChatController(private val chatService: ChatService) {


    @GetMapping("/chat_view/{chatId}/{viewerId}")
    fun getChatView(@PathVariable("chatId") chatId: UUID,
                    @PathVariable("viewerId") viewerId: UUID): ResponseEntity<ChatView> {

//        val chatView = ChatView.Builder()
//            .chatId(UUID.randomUUID())
//            .chatName("Sample Chat")
//            .lastMessage("Hello, World!")
//            .timestamp(Instant.now())
//            .build()

        val chatView = ChatView.Builder().fastBuild()


        return ResponseEntity.ok(chatView)
    }


}