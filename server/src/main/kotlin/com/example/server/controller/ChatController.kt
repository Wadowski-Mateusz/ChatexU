package com.example.server.controller

import com.example.server.dto.ChatView
import com.example.server.service.ChatService
import lombok.AllArgsConstructor
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
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

    @GetMapping("/chat_view")
    fun getChatView(): ResponseEntity<ChatView> {


        val chatView = ChatView.Builder().fastBuild()


        return ResponseEntity.ok(chatView)
    }

    @GetMapping(value = ["/icon"], produces = [MediaType.IMAGE_JPEG_VALUE])
    fun getPicture(@RequestParam userId: UUID?): ResponseEntity<ByteArray> {
        return try {
            // TODO change to URI
            val resource: Resource = ClassPathResource("icons/${listOf("red","green","blue").random()}.png")
            val inp = resource.inputStream.readAllBytes()
            ResponseEntity.ok(inp)
        } catch (e: Exception) {
            ResponseEntity.internalServerError().build()
        }
    }


}