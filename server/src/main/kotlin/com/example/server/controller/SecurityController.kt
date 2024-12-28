package com.example.server.controller

import com.example.server.dto.AuthenticationDTO
import com.example.server.dto.LoginDto
import com.example.server.dto.RegisterDto
import com.example.server.exceptions.BadLoginDataException
import com.example.server.exceptions.DataAlreadyInTheDatabaseException
import com.example.server.model.User
import com.example.server.service.UserService
import lombok.AllArgsConstructor
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.InternalAuthenticationServiceException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
@CrossOrigin
class SecurityController(
    private val userService: UserService,
    private val passwordEncoder: PasswordEncoder,
    private val authenticationManager: AuthenticationManager
) {

    private val logger = LoggerFactory.getLogger(UserService::class.java)

    @PostMapping("/register")
    fun registerUser(
        @RequestBody registerDto: RegisterDto
    ): ResponseEntity<AuthenticationDTO> {

        return try {
            val user = userService.createUserFromRegisterDto(registerDto.copy(
                password = passwordEncoder.encode(registerDto.password)
            ))

            val jwt: String = userService.createToken(user)
            val success = userService.saveToken(user, jwt)

            if(success) {
                ResponseEntity.status(HttpStatus.OK).body(AuthenticationDTO(token = jwt, userId = user.userId.toString()))
            }
            else {
                logger.error("register: Could not save JWT to the database")
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
            }
        } catch (e: DataAlreadyInTheDatabaseException) {
            logger.error("registerUser(): ${e::class.simpleName}")
            ResponseEntity.status(HttpStatus.CONFLICT).body(AuthenticationDTO(token = "${e.inDatabaseFlag}", userId = "0"))
        } catch (e: Exception) {
            logger.error("registerUser() unknown error- ${e::class.simpleName}")
            ResponseEntity.internalServerError().build()
        }

    }

    @PostMapping("/login")
    fun login(
        @RequestBody loginDto: LoginDto?
    ): ResponseEntity<AuthenticationDTO> {
        loginDto ?: return ResponseEntity(HttpStatus.BAD_REQUEST)
        return try {
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    loginDto.login,
                    loginDto.password
                )
            )

            val user: User = try {
                userService.getUserByLogin(loginDto.login)
            } catch (e: NullPointerException) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
            }
            val jwt: String = userService.createToken(user)
            val success = userService.saveToken(user, jwt)
            if(success) {
                ResponseEntity.status(HttpStatus.OK).body(AuthenticationDTO(jwt, user.userId.toString()))
            }
            else {
                logger.error("login(): Could not save JWT to the database")
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
            }

        } catch (e: BadLoginDataException) {
            logger.error("login e1: ${e::class.simpleName}\n${e.cause}")
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        } catch (e: InternalAuthenticationServiceException) {
            logger.error("login e2: ${e::class.simpleName}\n${e.cause}")
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        } catch (e: Exception) {
            logger.error("login e3: ${e::class.simpleName}\n${e.cause}")
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