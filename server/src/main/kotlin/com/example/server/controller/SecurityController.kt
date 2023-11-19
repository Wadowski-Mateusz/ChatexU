package com.example.server.controller

import com.example.server.dto.LoginDto
import com.example.server.dto.RegisterDto
import com.example.server.exceptions.BadLoginDataException
import com.example.server.exceptions.DataAlreadyInTheDatabaseException
import com.example.server.model.User
import com.example.server.service.UserService
import lombok.AllArgsConstructor
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/auth")
@AllArgsConstructor
@CrossOrigin
class SecurityController(
    private val userService: UserService
) {

    @PostMapping("/register")
    fun registerUser(@RequestBody registerDto: RegisterDto)
    : ResponseEntity<Any /*UserDto or error*/> {

        return try {
            val user = userService.createUserFromRegisterDto(registerDto)

            ResponseEntity(userService.convertToDto(user), HttpStatus.OK)
        } catch (exception: DataAlreadyInTheDatabaseException) {

            println("registerUser() - DataAlreadyInTheDatabase ${exception.message}")
            ResponseEntity.status(HttpStatus.CONFLICT).body(exception.message)

        } catch (exception: Exception) {
            println("registerUser() - ${exception.message}")
            ResponseEntity.internalServerError().build()
        }
    }


    @PostMapping("/login")
    fun login(@RequestBody loginDto: LoginDto): ResponseEntity<String> {

        println("login")
        return try {
            val user: User = userService.login(loginDto)
            println("success")
            ResponseEntity.status(HttpStatus.OK).body(user.userId.toString())
        } catch (e: BadLoginDataException) {
            println("failure")
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        } catch (e: Exception) {
            println("failure")
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }


    @PutMapping("/change_email/{userId}/{password}/{email}")
    fun changeEmail(
        @PathVariable("userId") userId: String,
        @PathVariable("password") password: String,
        @PathVariable("email") nickname: String,
    ): ResponseEntity<Boolean> {
        TODO("Not yet implemented")
    }

    @PutMapping("/change_password/{userId}/{oldPassword}/{newPassword}")
    fun changePassword(
        @PathVariable("userId") userId: String,
        @PathVariable("oldPassword") oldPassword: String,
        @PathVariable("newPassword") newPassword: String,
    ): ResponseEntity<Boolean> {
        TODO("Not yet implemented")
    }

}