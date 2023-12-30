package com.example.chatexu.converters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.example.chatexu.data.remote.dto.FriendDto
import com.example.chatexu.domain.model.Friend

object FriendMapper {
    fun toFriend(dto: FriendDto): Friend {
        val iconAsString = dto.icon
        val byteArrayData: ByteArray = Base64.decode(iconAsString, Base64.DEFAULT)
        val icon: Bitmap? = BitmapFactory.decodeByteArray(byteArrayData, 0, byteArrayData.size)

        return Friend(
            id = dto.id,
            nickname = dto.nickname,
            nicknameFromChat = dto.nicknameFromChat,
            icon = icon
        )

    }
}