package com.kokodi.service

import com.kokodi.entity.GameSession
import com.kokodi.entity.Player
import com.kokodi.entity.Turn
import com.kokodi.entity.cards.Card
import org.springframework.stereotype.Service

@Service
class TurnService {
    fun createTurn(gameSession: GameSession, activePlayer: Player, currentCard: Card): Turn {
        val turn = Turn(
            gameSession = gameSession,
            player = activePlayer,
            card = currentCard,
            step = (gameSession.turns.lastOrNull()?.step ?: 0) + 1
        )
        gameSession.turns.addLast(turn)
        return turn
    }

    fun getLastTurn(gameSession: GameSession): Turn {
        return gameSession.turns.last()
    }

    fun updateLastTurnPlayerUnderEffect(gameSession: GameSession, playerUnderEffect: Player) {
        val turn = getLastTurn(gameSession)
        turn.playerUnderEffect = playerUnderEffect
    }
}