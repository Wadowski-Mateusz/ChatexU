package com.example.server.dto

data class FriendDto(
    val id: String,
    val nickname: String,
    val nicknameFromChat: String,
    val icon: ByteArray,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FriendDto

        if (id != other.id) return false
        if (nickname != other.nickname) return false
        if (nicknameFromChat != other.nicknameFromChat) return false
        if (!icon.contentEquals(other.icon)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + nickname.hashCode()
        result = 31 * result + nicknameFromChat.hashCode()
        result = 31 * result + icon.contentHashCode()
        return result
    }
}

