package com.kokodi.security.service

import com.kokodi.security.dto.JwtAuthenticationResponse
import com.kokodi.security.dto.SignInRequest
import com.kokodi.security.dto.SignUpRequest
import com.kokodi.entity.User
import com.kokodi.service.UserService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthenticationService(
    private val userService: UserService,
    private val jwtService: JwtService,
    private val passwordEncoder: PasswordEncoder,
    private val authenticationManager: AuthenticationManager
) {

    /**
     * Регистрация пользователя
     */
    fun signUp(request: SignUpRequest): JwtAuthenticationResponse {
        val user = User(
            login = request.login,
            name = request.name,
            password = passwordEncoder.encode(request.password)
        )
        userService.create(user)
        val jwt = jwtService.generateToken(user)
        return JwtAuthenticationResponse(token = jwt)
    }

    /**
     * Авторизация пользователя
     */
    fun signIn(request: SignInRequest): JwtAuthenticationResponse {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(request.login, request.password)
        )
        val user = userService.getByLogin(request.login)
        val jwt = jwtService.generateToken(user)
        return JwtAuthenticationResponse(token = jwt)
    }
}
