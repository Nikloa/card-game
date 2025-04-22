package com.kokodi.entity

import com.kokodi.entity.cards.Card
import jakarta.persistence.*

@Entity
@Table(name = "turns")
data class Turn(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "turn_id_seq")
    @SequenceGenerator(name = "turn_id_seq", sequenceName = "turn_id_seq", allocationSize = 1)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "game_session_id", nullable = false)
    val gameSession: GameSession,

    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false)
    val player: Player,

    @ManyToOne
    @JoinColumn(name = "player_under_effect_id")
    var playerUnderEffect: Player? = null,

    @ManyToOne
    @JoinColumn(name = "card_id", nullable = false)
    val card: Card,

    @Column(nullable = false)
    val step: Int
)
