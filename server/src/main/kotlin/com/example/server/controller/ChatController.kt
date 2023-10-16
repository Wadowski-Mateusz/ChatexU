package com.example.server.controller

import com.example.server.converters.ChatMapper
import com.example.server.converters.MessageMapper
import com.example.server.dto.ChatDto
import com.example.server.dto.ChatViewDto
import com.example.server.dto.ChatViewIconDto
import com.example.server.dto.MessageDto
import com.example.server.exceptions.ChatNotFoundException
import com.example.server.exceptions.MessageNotFoundException
import com.example.server.exceptions.UserIsBlockedException
import com.example.server.exceptions.UserNotFoundException
import com.example.server.model.ChatType
import com.example.server.model.Message
import com.example.server.repository.MessageRepository
import com.example.server.service.ChatService
import lombok.AllArgsConstructor
import org.bson.types.ObjectId
import org.jetbrains.annotations.TestOnly
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.codec.DecoderHttpMessageReader
import org.springframework.web.bind.annotation.*
import java.time.Instant
import java.time.format.DateTimeParseException
import java.util.*


@RestController
@RequestMapping("/chat")
@AllArgsConstructor
@CrossOrigin
class ChatController(
    private val chatService: ChatService,
    private val messageRepository: MessageRepository
) {

    @GetMapping("/messages/all/{chatId}/{viewerId}")
    fun getFullChatHistory(
        @PathVariable("chatId") chatId: String,
        @PathVariable("viewerId") viewerId: String
//    ): ResponseEntity<List<Message>> {
    ): ResponseEntity<List<MessageDto>> {

        val messages: List<Message> = chatService.getAllChatMessages(chatId)

        return if (messages.isNotEmpty())
            ResponseEntity(
                messages.map {MessageMapper.messageToDto(it, viewerId)},
                HttpStatus.OK
            )
        else
            ResponseEntity(HttpStatus.NO_CONTENT)

    }

    @GetMapping("/messages/after/{chatId}/{after}/{viewerId}")
    fun getChatHistoryAfter(
        @PathVariable("chatId") chatId: String,
        @PathVariable("after") after: String,
        @PathVariable("viewerId") viewerId: String,
        ): ResponseEntity<List<MessageDto>> {
        return try {
            val messages: List<Message> = chatService.getChatMessagesAfter(chatId, Instant.parse(after))
            if (messages.isNotEmpty())
                ResponseEntity(
                    messages.map {MessageMapper.messageToDto(it, viewerId)},
                    HttpStatus.OK
                )
                else ResponseEntity(HttpStatus.NO_CONTENT)

        } catch (e: DateTimeParseException) {
            ResponseEntity(HttpStatus.BAD_REQUEST)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }


    @PostMapping("/create")
    fun createChat(
        @RequestBody() participants: List<String>
    ): ResponseEntity<String> { // TODO what should be a response?
        return try {
            val chatId: String =
                if (participants.isNotEmpty())
                    chatService.createChat(participants.toSet()).chatId.toHexString()
                else
                    ""
            ResponseEntity(chatId, HttpStatus.CREATED)
        } catch (e: UserIsBlockedException) {
            // TODO only some users can be blocked for group chat
            ResponseEntity(HttpStatus.FORBIDDEN)
        } catch (e: UserNotFoundException) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }

    }


    @GetMapping("/chat_view/chat_list/{userId}")
    fun getUserChats(
        @PathVariable("userId") userId: String
    ): ResponseEntity<List<ChatViewDto>> {
        // TODO
        println("Request - getUserChats()")
        return try {
            val chatList = chatService.findAllByUserId(userId)

            val chatViewDtoList = chatList. map {
                when(it.typeOfChat) {
                    is ChatType.UserToUser -> chatService.convertUserToUserChatToChatView(it, userId)
                    is ChatType.Group -> chatService.convertGroupChatToChatView(it)
                }
            }
            println("size = w${chatViewDtoList.size}, time = ${Instant.now()}")
            return ResponseEntity(chatViewDtoList, HttpStatus.OK)

        } catch (e: UserNotFoundException) {
            println("ChatController.getUserChats() - ${e.message}")
            ResponseEntity.badRequest().build()
        } catch (e: Exception) {
            println("ChatController.getChatList() - ${e.message}")
            ResponseEntity.internalServerError().build()
        }
    }


    @GetMapping("/chat_view/{chatId}/{viewerId}")
    fun getChatView(
        @PathVariable("chatId") chatId: String,
        @PathVariable("viewerId") viewerId: String
    ): ResponseEntity<ChatViewDto> {

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

    @TestOnly
    @GetMapping("/all")
    fun getAllChats(): ResponseEntity<List<ChatDto>> {
        return ResponseEntity(
            chatService.getAll().map{ChatMapper.convertToDto(it)},
            HttpStatus.OK
        )
    }

    @TestOnly
    @GetMapping(value = ["/icon"], produces = [MediaType.IMAGE_JPEG_VALUE])
    fun getPicture(): ResponseEntity<ByteArray> {
        return try {
            val resource: Resource = ClassPathResource("icons/${listOf("red","green","blue").random()}.png")
            val inp = resource.inputStream.readAllBytes()
            ResponseEntity.ok(inp)
        } catch (e: Exception) {
            ResponseEntity.internalServerError().build()
        }
    }

    @TestOnly
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