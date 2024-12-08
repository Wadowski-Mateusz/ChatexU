package com.example.server.controller

import com.example.server.commons.default
import com.example.server.converters.MessageMapper
import com.example.server.dto.MessageDto
import com.example.server.dto.SentMessageDto
import com.example.server.dto.UserDto
import com.example.server.exceptions.MessageNotFoundException
import com.example.server.exceptions.UserNotFoundException
import com.example.server.model.ChatType
import com.example.server.model.Message
import com.example.server.model.MessageType
import com.example.server.model.User
import com.example.server.service.MessageService
import lombok.AllArgsConstructor
import org.bson.types.ObjectId
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.time.Instant

@RestController
@RequestMapping("/message")
@AllArgsConstructor
@CrossOrigin
class MessageController(private val messageService: MessageService) {


    @GetMapping("/get/{messageId}/{viewerId}")
    fun findMessageById(
        @PathVariable("messageId") messageId: String,
        @PathVariable("viewerId") viewerId: String
    ): ResponseEntity<MessageDto> {
        return try {
            val message = messageService.findMessageById(messageId)
            ResponseEntity(
                messageService.convertMessageToDto(message, viewerId),
                HttpStatus.OK
            )
        } catch (e: MessageNotFoundException) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        } catch (e: Exception) {
            e.printStackTrace()
            println("MessageController.finMessageById() ${e.message}")
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @PostMapping("/send")
    fun sendMessage(@RequestBody sentMessageDto: SentMessageDto): ResponseEntity<MessageDto> {

        println(sentMessageDto.replyTo)
        val message = MessageMapper.fromSendedMessage(sentMessageDto)
        println(message.replyTo)
        val savedMessage = messageService.save(message)

        return ResponseEntity(
            messageService.convertMessageToDtoAsSender(savedMessage),
            HttpStatus.OK
        )
    }

    @PutMapping("/update_message")
    fun updateMessage(@RequestBody messageDto: MessageDto): ResponseEntity<MessageDto> {
        if (messageDto.messageType !is MessageType.Text)
            return ResponseEntity(null, HttpStatus.BAD_REQUEST)

        return try {
            val updatedMessage = messageService.updateTextMessage(messageDto.messageId, messageDto.messageType.text)
            ResponseEntity(
                messageService.convertMessageToDtoAsSender(updatedMessage),
                HttpStatus.OK
            )
        } catch (e: MessageNotFoundException) {
            ResponseEntity(null, HttpStatus.NOT_FOUND)
        }

    }

    @PutMapping("/delete_message/{messageId}/{userId}")
    fun deleteMessage(@PathVariable("messageId") messageId: String, @PathVariable userId: String): ResponseEntity<MessageDto> {
        val deletedMessage = messageService.deleteMessage(messageId, userId)
        return ResponseEntity(
            messageService.convertMessageToDto(deletedMessage, userId),
            HttpStatus.OK
        )
    }

    @GetMapping("/all")
    fun getAll(): ResponseEntity<List<Message>> {
        val messages: List<Message> = messageService.getAllMessages()
        return ResponseEntity.ok(messages)
    }


    @PutMapping("/sendImage")
    fun updateIcon(
        @RequestPart("image") image: MultipartFile,
        @RequestPart("message") sentMessageDto: SentMessageDto,
    ): ResponseEntity<String> {

        println("incoming message ${Instant.now()}")

        // Map dto to object
        val message: Message = MessageMapper.fromSendedMessage(sentMessageDto)

        // Save resource & save uri
        val imageUri: String = messageService.saveImage(image, message.chatId.toString())

        val resourceImage = message.copy(messageType = MessageType.Resource(imageUri))

        // save message
        val savedMessage: Message = messageService.save(resourceImage)

        // response
        return ResponseEntity(savedMessage.messageId.toString(), HttpStatus.OK)
    }

}