package com.kokodi.entity.cards

import com.kokodi.entity.GameSession
import jakarta.persistence.*

@Entity
@Table(name = "deck_cards")
data class DeckCard(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "deck_card_id_seq")
    @SequenceGenerator(name = "deck_card_id_seq", sequenceName = "deck_card_id_seq", allocationSize = 1)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "card_id", nullable = false)
    val card: Card,

    @ManyToOne
    @JoinColumn(name = "game_session_id")
    val gameSession: GameSession? = null,

    @Column(name = "deck_position", nullable = false)
    val position: Int
)
