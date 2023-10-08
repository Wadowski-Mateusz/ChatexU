package com.example.server.controller

import com.example.server.commons.Constants
import com.example.server.dto.RegisterDto
import com.example.server.dto.UserDto
import com.example.server.dto.toDto
import com.example.server.exceptions.DataAlreadyInTheDatabaseException
import com.example.server.model.User
import com.example.server.service.UserService
import lombok.AllArgsConstructor
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/auth")
@AllArgsConstructor
@CrossOrigin
class SecurityController(
    private val userService: UserService
) {

    @PostMapping("/register")
    fun registerUser(@RequestBody registerDto: RegisterDto): ResponseEntity<UserDto> {
        // TODO exceptions
        return try {
            val user = createUserFromRegisterDto(registerDto)
            val savedUser = userService.save(user)
            ResponseEntity.ok(savedUser.toDto())
        } catch (exception: DataAlreadyInTheDatabaseException) {
            ResponseEntity.status(HttpStatus.CONFLICT).build()
        } catch (exception: Exception) {
            println("registerUser() - ${exception.message}")
            ResponseEntity.internalServerError().build()
        }
    }

    private fun createUserFromRegisterDto(registerDto: RegisterDto): User {

        // TODO uncomment this line
//        verifyRegisterDto(registerDto)

        val user = User(
            nickname = registerDto.nickname,
            email = registerDto.email,
            login = registerDto.login,
            password = registerDto.password,
            profilePictureUri = Constants.DEFAULT_PROFILE_URI,
        )

        return userService.save(user)

    }


    private fun verifyRegisterDto(registerDto: RegisterDto) {
        TODO("Not yet implemented - check if data is already in the database")
        //
    }

}