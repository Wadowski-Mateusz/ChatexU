package com.example.server.controller

import com.example.server.dto.ChatViewDto
import com.example.server.dto.ChatViewIconDto
import com.example.server.dto.MessageDto
import com.example.server.dto.toDto
import com.example.server.service.ChatService
import com.example.server.service.MessageService
import lombok.AllArgsConstructor
import org.bson.types.ObjectId
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.Instant
import java.util.*


@RestController
@RequestMapping("/chat")
@AllArgsConstructor
@CrossOrigin
class ChatController(
    private val chatService: ChatService,
    private val messageService: MessageService,
) {

    @GetMapping("/chat_view")
    fun getChatView(): ResponseEntity<ChatViewDto> {
        val chatViewDto = ChatViewDto.Builder().fastBuild()
        return ResponseEntity.ok(chatViewDto)
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


    @GetMapping(
        value = ["/chat_view/{chatId}"],
//        produces = [MediaType.IMAGE_JPEG_VALUE],
    ) fun getChatView(
        @PathVariable("chatId") chatId: String
    ): ResponseEntity<ChatViewIconDto> {
        return try {
            // TODO
            println("Add URIs")
            val resource: Resource = ClassPathResource("icons/${listOf("red","green","blue").random()}.png")
            val inp = resource.inputStream.readAllBytes()

            val chatDto = ChatViewIconDto(
                chatId,
                "Test Name",
                "Test last message",
                Instant.now(),
                inp
            )
            return ResponseEntity.ok(chatDto)
        } catch (e: Exception) {
            ResponseEntity.internalServerError().build()
        }
    }

}