package com.kokodi.entity

import com.kokodi.entity.cards.DeckCard
import jakarta.persistence.*

@Entity
@Table(name = "game_sessions")
data class GameSession(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "game_session_id_seq")
    @SequenceGenerator(name = "game_session_id_seq", sequenceName = "game_session_id_seq", allocationSize = 1)
    val id: Long? = null,

    @OneToMany(mappedBy = "gameSession", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val players: MutableSet<Player> = mutableSetOf(),

    @Enumerated(EnumType.STRING)
    var state: State = State.WAIT_FOR_PLAYERS,

    @OneToMany(mappedBy = "gameSession", cascade = [CascadeType.ALL, CascadeType.REMOVE], fetch = FetchType.LAZY)
    var deck: MutableList<DeckCard> = mutableListOf(),

    @OneToMany(mappedBy = "gameSession", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val turns: MutableList<Turn> = mutableListOf(),
) {
    enum class State {
        WAIT_FOR_PLAYERS, IN_PROGRESS, FINISHED
    }
}
