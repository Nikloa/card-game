package com.kokodi.repository

import com.kokodi.entity.cards.DeckCard
import org.springframework.data.jpa.repository.JpaRepository

interface DeckCardRepository : JpaRepository<DeckCard, Long> {
}