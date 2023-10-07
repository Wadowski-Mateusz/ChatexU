package com.example.server.controller

import com.example.server.MessageNotFoundException
import com.example.server.dto.MessageDto
import com.example.server.dto.toDto
import com.example.server.repository.MessageRepository
import com.example.server.service.MessageService
import lombok.AllArgsConstructor
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.function.EntityResponse

@RestController
@RequestMapping("/message")
@AllArgsConstructor
@CrossOrigin
class MessageController(private val messageService: MessageService) {


    @GetMapping("/get/{messageId}")
    fun findMessageById(@PathVariable("messageId") messageId: String): ResponseEntity<MessageDto> {
        return try {
            val messageId22 = messageService.findMessageById(messageId)
                .toDto()
            println(messageId22.messageId)
            ResponseEntity.ok(messageService.findMessageById(messageId).toDto())
        } catch (e: MessageNotFoundException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

    @PostMapping("/send")
    fun sendMessage(@RequestBody messageDto: MessageDto): MessageDto {
        val message = messageDto.toMessage()
        val savedMessage = messageService.save(message)
        return savedMessage.toDto()
    }

}