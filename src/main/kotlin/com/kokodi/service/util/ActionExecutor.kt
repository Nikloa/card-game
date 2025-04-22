package com.kokodi.service.util

import com.kokodi.entity.GameSession
import com.kokodi.service.PlayerService
import com.kokodi.service.TurnService
import org.springframework.stereotype.Service

@Service
class ActionExecutor(
    private val playerService: PlayerService,
    private val turnService: TurnService,
    private val properties: GameProperties)
{

    fun executeAddPointsAction(gameSession: GameSession, value: Int) {
        val player = playerService.getCurrentSessionPlayer(gameSession)
        val turn = turnService.getLastTurn(gameSession)
        player.points = (player.points + value).coerceAtMost(properties.maxScore)
        turn.playerUnderEffect = player
    }

    fun executeBlockAction(gameSession: GameSession, value: Int) {
        val activePlayer = playerService.getActivePlayer(gameSession)
        playerService.setNextPlayerTurn(gameSession, activePlayer)
        turnService.updateLastTurnPlayerUnderEffect(gameSession, activePlayer)
    }

    fun executeStealAction(gameSession: GameSession, value: Int) {
        val turn = turnService.getLastTurn(gameSession)
        val player = playerService.getCurrentSessionPlayer(gameSession)
        if (turn.playerUnderEffect == null) {
            val activePlayer = playerService.getActivePlayer(gameSession)
            playerService.reassignPlayersTurn(activePlayer, player)
        } else {
            turn.playerUnderEffect?.let { playerUnderEffect ->
                minOf(value, playerUnderEffect.points).also {
                    player.points += it
                    playerUnderEffect.points -= it
                }
            }
        }
    }

    fun executeDoubleDownAction(gameSession: GameSession, value: Int) {
        val player = playerService.getCurrentSessionPlayer(gameSession)
        val turn = turnService.getLastTurn(gameSession)
        player.points = (player.points * value).coerceAtMost(properties.maxScore)
        turn.playerUnderEffect = player
    }
}