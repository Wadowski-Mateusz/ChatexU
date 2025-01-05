package com.example.server.security

import com.example.server.commons.Constants
import com.example.server.exceptions.UserNotFoundException
import com.example.server.model.User
import com.example.server.service.FriendRequestService
import com.example.server.service.UserService
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
//import lombok.RequiredArgsConstructor
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException


@Component
//@RequiredArgsConstructor
class JwtAuthenticationFilter : OncePerRequestFilter() {

    @Autowired @Lazy
    private lateinit var _usersService: UserService
    private val usersService: UserService by lazy { _usersService }


    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

//        require(usersService!= null) {"JwtAuthenticationFilter() - userService init error, userService is null"}

        val authHeader: String = request.getHeader(Constants.AUTH_HEADER) ?: ""
        if (!authHeader.startsWith(Constants.AUTH_HEADER_START)) {
            filterChain.doFilter(request, response)
            return
        }

        val jwt: String = authHeader.substring(Constants.AUTH_HEADER_START.length)
        val login: String = try {
            usersService.extractLogin(jwt)
        } catch (e: MalformedJwtException) {
            logger.error("JwtAuthenticationFilter() - invalid token form")
            return filterChain.doFilter(request, response)
        } catch (e: ExpiredJwtException) {
            return filterChain.doFilter(request, response)
        }


        val user: User = try {
            usersService.getUserByLogin(login)
        } catch (e: UserNotFoundException) {
            return filterChain.doFilter(request, response)
        }

        val validJWT: String = user.token

        if (SecurityContextHolder.getContext().authentication == null) {
            val securityUserDetails: UserDetails = usersService.loadUserByUsername(login)
            try {
                if (usersService.isTokenValid(jwt, securityUserDetails) && jwt == validJWT) {
                    val authToken = UsernamePasswordAuthenticationToken(
                        securityUserDetails,
                        null,
                        securityUserDetails.authorities
                    )
                    authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = authToken
                }
            } catch (e: ExpiredJwtException){
                filterChain.doFilter(request, response)
            }
        }

        filterChain.doFilter(request, response)
    }
}