package com.kokodi.service

import com.kokodi.entity.*
import com.kokodi.entity.cards.DeckCard
import com.kokodi.repository.GameSessionRepository
import com.kokodi.service.util.GameStateManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.Queue

@Service
class GameSessionService @Autowired constructor(
    private val manager: GameStateManager,
    private val repository: GameSessionRepository
) {

    fun getGameSession(gameId: Long): GameSession {
        return repository.findById(gameId).orElseThrow {
            RuntimeException("GameSession with id=$gameId does not exist")
        }
    }

    @Transactional
    fun createGameSession(player: Player, deck: MutableList<DeckCard>): GameSession {
        val gameSession = GameSession(
            players = mutableSetOf(player)
        )

        gameSession.deck = deck

        manager.createGame(gameSession)

        return repository.save(gameSession)
    }

    @Transactional
    fun joinGameSession(gameId: Long, player: Player): GameSession {
        val gameSession = getGameSession(gameId)

        manager.checkForPlayersMaxSize(gameSession)

        gameSession.players.add(player)
        return repository.save(gameSession)
    }

    @Transactional
    fun startGameSession(gameId: Long): GameSession {
        val gameSession = getGameSession(gameId)

        manager.validateGameWaitForPlayers(gameSession)
        manager.checkForPlayersMinSize(gameSession)
        manager.startGame(gameSession)

        setTurnOrder(gameSession)
        setFirstTurnForPlayer(gameSession)

        return repository.save(gameSession)
    }

    private fun setTurnOrder(gameSession: GameSession) {
        gameSession.players.forEachIndexed { index, player ->
            player.turnOrder = index
        }
    }

    private fun setFirstTurnForPlayer(gameSession: GameSession) {
        gameSession.players.find { player -> player.turnOrder == 0 }?.isPlayerTurn = true
    }

    fun saveGameSession(session: GameSession): GameSession {
        return repository.save(session)
    }
}
