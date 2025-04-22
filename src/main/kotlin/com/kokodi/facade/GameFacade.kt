package com.kokodi.facade

import com.kokodi.dto.GameDto
import com.kokodi.dto.TurnDto
import com.kokodi.entity.GameSession
import com.kokodi.entity.Player
import com.kokodi.mapper.GameMapper
import com.kokodi.service.CardService
import com.kokodi.service.GameLogicService
import com.kokodi.service.GameSessionService
import com.kokodi.service.PlayerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class GameFacade @Autowired constructor(
    private val gameSessionService: GameSessionService,
    private val playerService: PlayerService,
    private val cardService: CardService,
    private val gameLogicService: GameLogicService,
    private val gameMapper: GameMapper,
) {

    fun createGameSession(): GameDto {
        val currentPlayer = playerService.getCurrentPlayer()
        val newDeck = cardService.createDeck()
        return gameMapper.toGameDto(gameSessionService.createGameSession(currentPlayer, newDeck))
    }

    fun joinGameSession(gameId: Long): GameDto {
        val currentPlayer = playerService.getCurrentPlayer()
        return gameMapper.toGameDto(gameSessionService.joinGameSession(gameId, currentPlayer))
    }

    fun startGameSession(gameId: Long): GameDto {
        return gameMapper.toGameDto(gameSessionService.startGameSession(gameId))
    }

    fun stepGameSession(gameId: Long): GameDto {
        return gameMapper.toGameDto(gameLogicService.stepGameSession(gameId))
    }

    fun stealAction(gameId: Long, playerId: Long): GameDto {
        return gameMapper.toGameDto(gameLogicService.stealAction(gameId, playerId))
    }

    fun getHistory(gameId: Long): List<TurnDto> {
        val gameSession = gameSessionService.getGameSession(gameId)
        return gameSession.turns.map(gameMapper::toTurnDto)
    }
}