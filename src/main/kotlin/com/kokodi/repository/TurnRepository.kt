package com.kokodi.repository

import com.kokodi.entity.Turn
import org.springframework.data.jpa.repository.JpaRepository

interface TurnRepository : JpaRepository<Turn, Long> {
}