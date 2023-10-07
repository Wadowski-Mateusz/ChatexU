package com.example.server.controller

import com.example.server.dto.ChatViewDto
import com.example.server.dto.MessageDto
import com.example.server.dto.toDto
import com.example.server.service.ChatService
import com.example.server.service.MessageService
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
class ChatController(
    private val chatService: ChatService,
    private val messageService: MessageService,
) {



    @GetMapping("/chat_view/{chatId}/{viewerId}")
    fun getChatView(@PathVariable("chatId") chatId: UUID,
                    @PathVariable("viewerId") viewerId: UUID): ResponseEntity<ChatViewDto> {

//        val chatView = ChatView.Builder()
//            .chatId(UUID.randomUUID())
//            .chatName("Sample Chat")
//            .lastMessage("Hello, World!")
//            .timestamp(Instant.now())
//            .build()

        val chatViewDto = ChatViewDto.Builder().fastBuild()


        return ResponseEntity.ok(chatViewDto)
    }

    @GetMapping("/chat_view")
    fun getChatView(): ResponseEntity<ChatViewDto> {
        val chatViewDto = ChatViewDto.Builder().fastBuild()
        return ResponseEntity.ok(chatViewDto)
    }

    @GetMapping("/chat_views_test")
    fun getChatViewsTest(): ResponseEntity<List<ChatViewDto>> {
        val l =
            generateSequence {
                ChatViewDto.Builder().fastBuild()
            }
                .take(25)
                .toList()
        return ResponseEntity.ok(l)
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