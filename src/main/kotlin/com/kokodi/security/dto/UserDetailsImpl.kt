package com.kokodi.security.dto

import com.kokodi.entity.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserDetailsImpl(private val user: User) : UserDetails {

    override fun getUsername(): String {
        return user.login
    }

    override fun getPassword(): String {
        return user.password
    }

    override fun getAuthorities(): Collection<GrantedAuthority> {
        // Здесь можно вернуть роли пользователя
        return emptyList() // Пример, возвращающий пустой список
    }

    override fun isAccountNonExpired(): Boolean {
        return true // Логика проверки
    }

    override fun isAccountNonLocked(): Boolean {
        return true // Логика проверки
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true // Логика проверки
    }

    override fun isEnabled(): Boolean {
        return true // Логика проверки
    }
}