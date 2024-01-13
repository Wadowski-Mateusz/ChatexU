package com.example.chatexu.converters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.example.chatexu.data.remote.dto.UserDto
import com.example.chatexu.data.remote.dto.UserViewDto
import com.example.chatexu.domain.model.Friend
import com.example.chatexu.domain.model.User

object UserMapper {

    fun toUser(userDto: UserDto): User {
        val iconAsString = userDto.profilePicture
        val byteArrayData: ByteArray = Base64.decode(iconAsString, Base64.DEFAULT)
        val icon: Bitmap? = BitmapFactory.decodeByteArray(byteArrayData, 0, byteArrayData.size)

        return User(
            id = userDto.userId,
            nickname = userDto.nickname,
            friends = userDto.friends.map { it.toString() },
            icon = icon,
        )
    }

    fun toUser(userViewDto: UserViewDto): User {
        val iconAsString = userViewDto.icon
        val byteArrayData: ByteArray = Base64.decode(iconAsString, Base64.DEFAULT)
        val icon: Bitmap? = BitmapFactory.decodeByteArray(byteArrayData, 0, byteArrayData.size)

        return User(
            id = userViewDto.id,
            nickname = userViewDto.nickname,
            icon = icon,
            friends = emptyList(),
        )
    }

    fun toUser(friend: Friend): User {
        return User(
            id = friend.id,
            nickname = friend.nickname,
            icon = friend.icon,
            friends = emptyList(),
        )
    }





}