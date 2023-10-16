package com.example.chatexu.converters

import com.example.chatexu.data.remote.dto.UserDto
import com.example.chatexu.domain.model.User

object UserMapper {

    fun toUser(userDto: UserDto): User {
        return User(
            id = userDto.userId,
            nickname = userDto.nickname,
        )
    }


}