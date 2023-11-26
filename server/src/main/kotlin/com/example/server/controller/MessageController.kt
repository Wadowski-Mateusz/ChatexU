package com.example.server.controller

import com.example.server.commons.default
import com.example.server.converters.MessageMapper
import com.example.server.dto.MessageDto
import com.example.server.dto.SendedMessageDto
import com.example.server.exceptions.MessageNotFoundException
import com.example.server.model.Message
import com.example.server.model.MessageType
import com.example.server.service.MessageService
import lombok.AllArgsConstructor
import org.bson.types.ObjectId
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
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
    fun sendMessage(@RequestBody sendedMessageDto: SendedMessageDto): ResponseEntity<MessageDto> {

        println("incoming message ${Instant.now()}") // TODO delete
        println(ObjectId().default()) // TODO delete
        println(sendedMessageDto.replyTo)
        val message = MessageMapper.fromSendedMessage(sendedMessageDto)
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

}