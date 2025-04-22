package com.kokodi.service

import com.kokodi.entity.GameSession
import com.kokodi.entity.Player
import com.kokodi.entity.User
import com.kokodi.exception.PlayerNotActiveException
import com.kokodi.exception.PlayerNotFoundException
import com.kokodi.exception.ThereNoActivePlayersException
import com.kokodi.repository.CardRepository
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class PlayerService(
    private val userService: UserService
) {

    fun getCurrentPlayer(): Player {
        return Player(user = getCurrentUser())
    }

    fun getCurrentSessionPlayer(session: GameSession): Player {
        val user = getCurrentUser()
        return session.players.find { it.user.id == user.id } ?: getCurrentPlayer()
    }

    private fun getCurrentUser(): User {
        return userService.getByLogin(SecurityContextHolder.getContext().authentication.name)
    }

    fun validatePlayerTurn(activePlayer: Player) {
        val currentUser = getCurrentUser()
        if (activePlayer.user.id != currentUser.id) {
            throw PlayerNotActiveException("It's not your turn, ${currentUser.name}.")
        }
    }

    fun getActivePlayer(gameSession: GameSession): Player {
        return gameSession.players.find { it.isPlayerTurn } ?: throw ThereNoActivePlayersException("No active player found.")
    }

    fun setNextPlayerTurn(gameSession: GameSession, activePlayer: Player) {
        val nextPlayer = getNextPlayer(gameSession, activePlayer)
        reassignPlayersTurn(activePlayer, nextPlayer)
    }

    fun reassignPlayersTurn(activePlayer: Player, nextPlayer: Player) {
        activePlayer.isPlayerTurn = false
        nextPlayer.isPlayerTurn = true
    }

    private fun getNextPlayer(gameSession: GameSession, activePlayer: Player): Player {
        val nextIndex = (activePlayer.turnOrder + 1) % gameSession.players.size
        return gameSession.players.elementAt(nextIndex)
    }

    fun getPlayerWithSession(playerId: Long, gameSession: GameSession): Player {
        return gameSession.players.find { it.id == playerId } ?:
            throw PlayerNotFoundException("Player with id=$playerId does not exist in game")
    }
}
