package com.kokodi.repository

import com.kokodi.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findByLogin(login: String): User?
    fun existsByLogin(login: String): Boolean
}
