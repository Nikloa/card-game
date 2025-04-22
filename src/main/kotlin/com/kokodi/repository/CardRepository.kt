package com.kokodi.repository

import com.kokodi.entity.cards.Card
import org.springframework.data.jpa.repository.JpaRepository

interface CardRepository : JpaRepository<Card, Long> {
}