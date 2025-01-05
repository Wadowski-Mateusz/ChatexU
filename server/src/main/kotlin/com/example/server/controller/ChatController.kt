package com.example.server.controller

import com.example.server.converters.ChatMapper
import com.example.server.dto.*
import com.example.server.exceptions.ChatNotFoundException
import com.example.server.exceptions.UserIsBlockedException
import com.example.server.exceptions.UserNotFoundException
import com.example.server.model.Chat
import com.example.server.model.ChatType
import com.example.server.model.Message
import com.example.server.service.ChatService
import com.example.server.service.MessageService
import com.example.server.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.Instant
import java.time.format.DateTimeParseException


@RestController
@RequestMapping("/chat")
class ChatController(
    private val chatService: ChatService,
    private val userService: UserService,
    private val messageService: MessageService,
) {

    @GetMapping("/messages/all/{chatId}/{viewerId}")
    fun getFullChatHistory(
        @PathVariable("chatId") chatId: String,
        @PathVariable("viewerId") viewerId: String
    ): ResponseEntity<List<MessageDto>> {

        val messages: List<Message> = chatService.getAllChatMessages(chatId)

        return if (messages.isNotEmpty()) {
            ResponseEntity(
                messages.map { messageService.toDto(it, viewerId) },
                HttpStatus.OK
            )
        }
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
                    messages.map { messageService.toDto(it, viewerId) },
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

    @GetMapping("/chat_view/chat_list/{userId}")
    fun getUserChats(
        @PathVariable("userId") userId: String
    ): ResponseEntity<List<ChatViewDto>> {
        return try {
            val chatList = chatService.findAllChatsByUserId(userId)
            val chatViewDtoList = chatList.map {
                when(it.typeOfChat) {
                    is ChatType.UserToUser -> chatService.convertUserToUserChatToChatView(it, userId)
                    is ChatType.Group -> chatService.convertGroupChatToChatView(it)
                }
            }
            ResponseEntity(chatViewDtoList, HttpStatus.OK)
        } catch (e: UserNotFoundException) {
            ResponseEntity.badRequest().build()
        } catch (e: Exception) {
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


    @GetMapping ("/get/chat_participants/{chatId}")
    fun getChatParticipants(
        @PathVariable("chatId") chatId: String
    ): ResponseEntity<List<UserDto>> {

        return try {
            val chat: Chat = chatService.findChatById(chatId)

            val body: List<UserDto> = when (chat.typeOfChat) {
                is ChatType.UserToUser -> {
                    chat.participants.map { userService.convertToDto(it.toString()) }
                }
                is ChatType.Group -> {
                    TODO("Not yet implemented - chatController.getChatParticipants()")
                }
            }

            return ResponseEntity(body, HttpStatus.OK)

        } catch (e: ChatNotFoundException) {
            ResponseEntity.badRequest().build()
        } catch(e: Exception) {
            println("ChatController.getChatView() - ${e.message}")
            ResponseEntity.internalServerError().build()
        }

    }


    @GetMapping("/get/{chatId}")
    fun getChat(
        @PathVariable("chatId") chatId: String
    ): ResponseEntity<ChatDto> {

        return try {
            val chat = chatService.findChatById(chatId)
            return ResponseEntity(
                ChatMapper.toDto(chat),
                HttpStatus.OK
            )
        } catch (e: ChatNotFoundException) {
            ResponseEntity.badRequest().build()
        } catch(e: Exception) {
            println("ChatController.getChatView() - ${e.message}")
            ResponseEntity.internalServerError().build()
        }

    }

    @PostMapping("/create")
    fun createChat(
        @RequestBody participantsDto: ParticipantsDto
    ): ResponseEntity<String> {

        val participants = participantsDto.participants
        return try {
            val chatId: String =
                if (participants.isNotEmpty())
                    chatService.getOrCreateChat(participants.toSet()).chatId.toHexString()
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

}