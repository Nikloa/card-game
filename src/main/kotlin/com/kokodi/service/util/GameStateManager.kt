package com.kokodi.service.util

import com.kokodi.entity.GameSession
import com.kokodi.entity.Player
import com.kokodi.exception.*
import com.kokodi.service.PlayerService
import com.kokodi.service.TurnService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import kotlin.math.max

@Service
class GameStateManager(
    private val properties: GameProperties
) {

    companion object {

        // Тэтраидальная яйцечистилка
        // Сверхплазменные обнимашки с дедом
        // Туалетка Шредингера
        // Помойная губка мерзости
        // Рвота Уробороса
    }

    fun checkForPlayersMinSize(gameSession: GameSession) {
        if (gameSession.players.size < properties.minPlayers) {
            throw NotEnoughPlayersException("Not enough players to start the game.")
        }
    }

    fun checkForPlayersMaxSize(gameSession: GameSession) {
        if (gameSession.players.size >= properties.maxPlayers) {
            throw TooManyPlayersException("Cannot join: too many players.")
        }
    }

    fun validateGameWaitForPlayers(gameSession: GameSession) {
        if (gameSession.state != GameSession.State.WAIT_FOR_PLAYERS) {
            throw GameNotWaitingForPlayersException("Game already in progress or finished.")
        }
    }

    fun validateGameInProgress(gameSession: GameSession) {
        if (gameSession.state != GameSession.State.IN_PROGRESS) {
            throw GameNotInProgressException("Game is not currently in progress.")
        }
    }

    fun isDeckEmpty(gameSession: GameSession): Boolean {
        return gameSession.deck.size <= 0
    }

    fun validateWinCondition(gameSession: GameSession) {
        if (isWinCondition(gameSession) || isDeckEmpty(gameSession)) {
            finishGame(gameSession)
        }
    }

    fun isWinCondition(gameSession: GameSession): Boolean {
        return gameSession.players.any { it.points >= properties.maxScore }
    }

    fun createGame(gameSession: GameSession) {
        gameSession.state = GameSession.State.WAIT_FOR_PLAYERS
    }

    fun startGame(gameSession: GameSession) {
        gameSession.state = GameSession.State.IN_PROGRESS
    }

    fun finishGame(gameSession: GameSession) {
        gameSession.state = GameSession.State.FINISHED
    }

    fun validateDifferentPlayer(activePlayer: Player, playerUnderEffect: Player) {
        if (activePlayer == playerUnderEffect) {
            throw SamePlayerException("Chosen player should be different with active player")
        }
    }
}
