package com.example.server.commons

object Constants {

    const val ROLE_USER: String = "user"
    const val ROLE_ADMIN: String = "admin"

    const val AUTH_HEADER: String = "Authorization"
    const val AUTH_HEADER_START: String = "Bearer "


    private const val ICON_DIRECTORY = "icons/"
    const val DEFAULT_PROFILE_URI = "${ICON_DIRECTORY}default.png"
}