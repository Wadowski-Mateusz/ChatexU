package com.example.server.controller

import com.example.server.dto.ChatViewDto
import com.example.server.dto.ChatViewIconDto
import com.example.server.exceptions.ChatNotFoundException
import com.example.server.exceptions.UserNotFoundException
import com.example.server.model.ChatType
import com.example.server.service.ChatService
import com.example.server.service.MessageService
import com.example.server.service.UserService
import lombok.AllArgsConstructor
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
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
) {


    @GetMapping("/chat_view/chat_list/{userId}")
    fun getChatList(@PathVariable("userId") userId: String): ResponseEntity<List<ChatViewDto>> {
        return try {
            val chatList = chatService.findAllByUserId(userId)

            TODO("Converting to dtos")
//            ResponseEntity.ok(chatList)
        } catch (e: UserNotFoundException) {
            ResponseEntity.badRequest().build()
        } catch (e: Exception) {
            println("ChatController.getChatList() - ${e.message}")
            ResponseEntity.internalServerError().build()
        }
    }


    @GetMapping("/chat_view/{chatId}/{viewerId}")
    fun getChatView(@PathVariable("chatId") chatId: String, @PathVariable("viewerId") viewerId: String): ResponseEntity<ChatViewDto> {

        return try {
            val chat = chatService.findChatById(chatId)

            return when (chat.typeOfChat) {
                is ChatType.UserToUser -> {
                    val chatViewDto = chatService.convertUserToUserChatToChatView(chat, viewerId)
                    ResponseEntity(chatViewDto, HttpStatus.OK)
                }
                is ChatType.Group -> {
                    TODO("Not yet implemented")
                }
            }

        } catch (e: ChatNotFoundException) {
            ResponseEntity.badRequest().build()
        } catch(e: Exception) {
            println("ChatController.getChatView() - ${e.message}")
            ResponseEntity.internalServerError().build()
        }
    }


    /*
    * UNUSED BELOW
    * */

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