package com.kokodi.service

import com.kokodi.entity.GameSession
import com.kokodi.service.util.ActionExecutor
import com.kokodi.service.util.GameStateManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GameLogicService @Autowired constructor(
    private val gameSessionService: GameSessionService,
    private val playerService: PlayerService,
    private val turnService: TurnService,
    private val cardService: CardService,
    private val manager: GameStateManager,
    private val actionExecutor: ActionExecutor
) {

    @Transactional
    fun stepGameSession(gameId: Long): GameSession {
        val gameSession = gameSessionService.getGameSession(gameId)
        manager.validateGameInProgress(gameSession)

        val activePlayer = playerService.getActivePlayer(gameSession)
        playerService.validatePlayerTurn(activePlayer)
        playerService.setNextPlayerTurn(gameSession, activePlayer)

        val deckCard = cardService.getCurrentDeckCard(gameSession)

        turnService.createTurn(gameSession, activePlayer, deckCard.card)

        deckCard.card.action(gameSession, actionExecutor)

        manager.validateWinCondition(gameSession)

        return gameSessionService.saveGameSession(gameSession)
    }

    @Transactional
    fun stealAction(gameId: Long, playerId: Long): GameSession {
        val gameSession = gameSessionService.getGameSession(gameId)
        manager.validateGameInProgress(gameSession)

        val activePlayer = playerService.getActivePlayer(gameSession)
        playerService.validatePlayerTurn(activePlayer)

        val lastCard = cardService.getLastCard(gameSession)
        cardService.validateCardTypeSteal(lastCard)

        val playerUnderEffect = playerService.getPlayerWithSession(playerId, gameSession)

        manager.validateDifferentPlayer(activePlayer, playerUnderEffect)

        playerService.setNextPlayerTurn(gameSession, activePlayer)

        turnService.updateLastTurnPlayerUnderEffect(gameSession, playerUnderEffect)

        lastCard.action(gameSession, actionExecutor)

        manager.validateWinCondition(gameSession)

        return gameSessionService.saveGameSession(gameSession)
    }
}
