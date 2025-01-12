package com.example.server.security

import com.example.server.commons.Constants.ROLE_USER
import com.example.server.commons.Constants.ROLE_ADMIN
import com.example.server.service.UserService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
//import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.authentication.logout.LogoutHandler
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy

@Configuration
@EnableWebSecurity
//@RequiredArgsConstructor
class SecurityConfig(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val authenticationProvider: AuthenticationProvider,

) {

//    private val logoutHandler: LogoutHandler

    @Lazy
    @Autowired
    private val _logoutHandler: LogoutHandler? = null
    private val logoutHandler: LogoutHandler by lazy { _logoutHandler!! }

    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests{
                it
//                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//                    .requestMatchers(HttpMethod.OPTIONS, "/test").permitAll()
//                    .requestMatchers(HttpMethod.OPTIONS, "/test/**").permitAll()
                    .requestMatchers("/chat/**").hasAnyRole(ROLE_USER, ROLE_ADMIN)
                    .requestMatchers("/message/**").hasAnyRole(ROLE_USER, ROLE_ADMIN)
                    .requestMatchers("/user/**").hasAnyRole(ROLE_USER, ROLE_ADMIN)
                    .requestMatchers("/test/**").hasRole(ROLE_ADMIN)
                    .requestMatchers("/auth/**").permitAll() // login, register
//                    .anyRequest().authenticated()
//                    .requestMatchers("/test/**").permitAll()
//                    .requestMatchers("/test/hello").permitAll()
                    .anyRequest().hasRole(ROLE_ADMIN)
            }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .logout {
                it.addLogoutHandler(logoutHandler)
                    .logoutSuccessHandler { _, _, _ -> SecurityContextHolder.clearContext() }
            }


        return http.build()
    }
}