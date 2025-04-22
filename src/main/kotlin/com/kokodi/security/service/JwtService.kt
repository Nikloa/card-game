package com.kokodi.security.service

import com.kokodi.entity.User
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.security.Key
import java.util.Date
import javax.crypto.SecretKey

@Service
class JwtService {

    @Value("\${token.signing.key}")
    private lateinit var SECRET_KEY: String
    private val EXPIRATION_TIME = 1000 * 60 * 15

    // Генерация токена
    fun generateToken(user: User): String {
        val claims = mutableMapOf<String, Any>().apply {
            user.id?.let { put("id", it) }               // Сохраняем идентификатор пользователя
            put("name", user.name)           // Сохраняем имя пользователя
            put("login", user.login)         // Сохраняем логин пользователя
            // Можно добавить другие данные, если необходимо
        }
        return createToken(claims, user.login)
    }

    // Создание токена
    private fun createToken(claims: Map<String, Any>, subject: String): String {
        require(SECRET_KEY.isNotBlank()) { "SECRET_KEY must not be blank" }
        val key: Key = Keys.hmacShaKeyFor(SECRET_KEY.toByteArray())
        return Jwts.builder()
            .claims(claims)
            .subject(subject)
            .issuedAt(Date())
            .expiration(Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .signWith(key)
            .compact()
    }

    // Валидация токена
    fun isTokenValid(token: String, login: String): Boolean {
        val loginFromToken = extractLogin(token)
        return (loginFromToken == login && !isTokenExpired(token))
    }

    // Извлечение имени пользователя
    fun extractLogin(token: String): String {
        if (token.isBlank()) {
            throw IllegalArgumentException("Token must not be null or empty")
        }
        return extractAllClaims(token).subject
    }

    // Извлечение всех данных из токена
    private fun extractAllClaims(token: String): Claims {
        val key: SecretKey = Keys.hmacShaKeyFor(SECRET_KEY.toByteArray())
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token).payload
    }

    // Проверка на истечение токена
    private fun isTokenExpired(token: String): Boolean {
        return extractAllClaims(token).expiration.before(Date())
    }
}
