package com.example.server.controller

import com.example.server.commons.Constants
import com.example.server.dto.RegisterDto
import com.example.server.dto.UserDto
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
    fun registerUser(@RequestBody registerDto: RegisterDto): ResponseEntity<UserDto> {
        // TODO exceptions
        return try {
            val user = createUserFromRegisterDto(registerDto)
            val savedUser = userService.save(user)

            ResponseEntity(userService.convertToDto(savedUser), HttpStatus.OK)
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
            password = registerDto.password,
            profilePictureUri = Constants.DEFAULT_PROFILE_URI,
            friends = setOf(),
            blockedUsers = setOf(),
        )

        return userService.save(user)

    }


    private fun verifyRegisterDto(registerDto: RegisterDto) {
        TODO("Not yet implemented - check if data is already in the database")
        //
    }

    // TODO move to security controller
    @PutMapping("/change_email/{userId}/{password}/{email}")
    fun changeEmail(
        @PathVariable("userId") userId: String,
        @PathVariable("password") password: String,
        @PathVariable("email") nickname: String,
    ): ResponseEntity<Boolean> {
        TODO("Not yet implemented")
    }

    // TODO move to security controller
    @PutMapping("/change_password/{userId}/{oldPassword}/{newPassword}")
    fun changePassword(
        @PathVariable("userId") userId: String,
        @PathVariable("oldPassword") oldPassword: String,
        @PathVariable("newPassword") newPassword: String,
    ): ResponseEntity<Boolean> {
        TODO("Not yet implemented")
    }

}