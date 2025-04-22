package com.kokodi.entity

import jakarta.persistence.*

@Entity
@Table(name = "players")
data class Player(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "player_id_seq")
    @SequenceGenerator(name = "player_id_seq", sequenceName = "player_id_seq", allocationSize = 1)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @ManyToOne
    @JoinColumn(name = "game_session_id")
    val gameSession: GameSession? = null,

    @Column(name = "points", nullable = false)
    var points: Int = 0,

    @Column(name = "turn_order", nullable = false)
    var turnOrder: Int = 0,

    @Column
    var isPlayerTurn: Boolean = false
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Player) return false
        return user.id == other.user.id
    }

    override fun hashCode(): Int {
        return user.id.hashCode()
    }
}
