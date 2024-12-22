package com.example.server.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.io.File


@Document("users")
data class User (

    @Id
    @Field("_id")
    val userId: ObjectId = ObjectId(),

    // visible by other users
    @Indexed(unique = true)
    val nickname: String,

    // Authentication only, not visible by other users
    @Indexed(unique = true)
    private val username: String, // private to avoid "same HVM signature" error

    @Indexed(unique = true)
    val email: String,
    private val password: String, // private to avoid "same HVM signature" error
    val profilePictureUri: String,

    val friends: Set<String>,
    val blockedUsers: Set<String>,

    val tokens: Map<String, Boolean> = emptyMap<String, Boolean>(), // <token, is expired>
    val role: String,

//    val lastTimeOnline: Instant = Instant.MIN, // when to update it?
//    val lastPassword: String = "",
//    val passwordResetTime: Instant = Instant.MIN
//    val status: ,    // sealed class, enum?
//    val settings // languages

): UserDetails {
    // TODO hardcoded icons path
    fun getIconAsByteArray(): ByteArray {
        val iconPath = this.profilePictureUri.removePrefix("icons/")
        val resourceFolder: File = File("src/main/resources/icons")
        val resource: File = File(resourceFolder, iconPath)
        return resource.inputStream().readAllBytes()
    }

    override fun getAuthorities(): Collection<GrantedAuthority?> {
        return listOf(SimpleGrantedAuthority("ROLE_$role"))
    }

    override fun getPassword(): String = password
    override fun getUsername(): String = email

    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean  = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isEnabled(): Boolean = true

}
