package com.kokodi.security.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class SignUpRequest(
    @field:NotBlank(message = "Имя пользователя не может быть пустыми")
    @field:Size(min = 1, max = 50, message = "Имя пользователя должно содержать от 1 до 50 символов")
    val name: String,

    @field:NotBlank(message = "Логин не может быть пустыми")
    @field:Size(min = 5, max = 50, message = "Логин должен содержать от 5 до 50 символов")
    val login: String,

    @field:NotBlank(message = "Пароль не может быть пустыми")
    @field:Size(max = 255, message = "Длина пароля должна быть не более 255 символов")
    val password: String
)
