package com.kokodi.security.service

import com.kokodi.security.dto.UserDetailsImpl
import com.kokodi.entity.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import com.kokodi.service.UserService

@Service
class UserDetailsServiceImpl(private val userService: UserService) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user: User = userService.getByLogin(username)
        return UserDetailsImpl(user)
    }
}