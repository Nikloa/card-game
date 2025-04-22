package com.kokodi.security.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class SignInRequest(
    @field:NotBlank(message = "Логин не может быть пустыми")
    @field:Size(min = 5, max = 50, message = "Логин должен содержать от 5 до 50 символов")
    val login: String,

    @field:NotBlank(message = "Пароль не может быть пустыми")
    @field:Size(min = 8, max = 255, message = "Длина пароля должна быть от 8 до 255 символов")
    val password: String
)