package com.kokodi.repository

import com.kokodi.entity.GameSession
import org.springframework.data.jpa.repository.JpaRepository

interface GameSessionRepository : JpaRepository<GameSession, Long> {
}