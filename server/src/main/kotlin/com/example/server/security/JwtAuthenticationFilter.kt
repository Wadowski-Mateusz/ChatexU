package com.example.server.security

import com.example.server.commons.Constants
import com.example.server.service.UserService
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.RequiredArgsConstructor
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
@RequiredArgsConstructor
class JwtAuthenticationFilter : OncePerRequestFilter() {

    @Autowired
    private val usersService: UserService? = null

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        require(usersService!= null) {"JwtAuthenticationFilter() - userService init error, userService is null"}

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

        val user = usersService.getUserByLogin(login)
        val validJWT = user.tokens.keys.first()

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