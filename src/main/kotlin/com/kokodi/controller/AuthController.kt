package com.kokodi.controller

import com.kokodi.security.service.AuthenticationService
import com.kokodi.security.dto.JwtAuthenticationResponse
import com.kokodi.security.dto.SignInRequest
import com.kokodi.security.dto.SignUpRequest
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(private val authenticationService: AuthenticationService) {

    /**
     * Эндпоинт для регистрации
     */
    @PostMapping("/sign-up")
    fun signUp(@RequestBody request: SignUpRequest): JwtAuthenticationResponse {
        return authenticationService.signUp(request)
    }

    /**
     * Эндпоинт для авторизации
     */
    @PostMapping("/sign-in")
    fun signIn(@RequestBody request: SignInRequest): JwtAuthenticationResponse {
        return authenticationService.signIn(request)
    }
}
