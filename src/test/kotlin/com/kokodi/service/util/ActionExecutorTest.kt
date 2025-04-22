package com.kokodi.service.util

import com.kokodi.entity.Turn
import com.kokodi.service.PlayerService
import com.kokodi.service.TurnService
import com.kokodi.util.TestData
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockitoExtension::class)
class ActionExecutorTest {

    @InjectMocks
    private lateinit var actionExecutor: ActionExecutor

    @Mock
    private lateinit var playerService: PlayerService

    @Mock
    private lateinit var turnService: TurnService

    @Mock
    private lateinit var properties: GameProperties

    @Test
    fun `executeAddPointsAction should add points to current player respecting maxScore`() {
        val gameSession = TestData.createGameSession()
        val player = TestData.createFirstPlayer(points = 10)
        val card = TestData.createPointCard()
        val turn = TestData.createTurn(gameSession, player, null, card)

        `when`(playerService.getCurrentSessionPlayer(gameSession)).thenReturn(player)
        `when`(turnService.getLastTurn(gameSession)).thenReturn(turn)
        `when`(properties.maxScore).thenReturn(15)

        actionExecutor.executeAddPointsAction(gameSession, 5)

        assertAll(
            { assertEquals(15, player.points) },
            { assertEquals(player, turn.playerUnderEffect) }
        )
    }

    @Test
    fun `executeBlockAction should set next player turn and update last turn`() {
        val gameSession = TestData.createGameSession()
        val activePlayer = TestData.createFirstPlayer()

        `when`(playerService.getActivePlayer(gameSession)).thenReturn(activePlayer)

        actionExecutor.executeBlockAction(gameSession, 0)

        verify(playerService).setNextPlayerTurn(gameSession, activePlayer)
        verify(turnService).updateLastTurnPlayerUnderEffect(gameSession, activePlayer)
    }

    @Test
    fun `executeStealAction should steal points from the affected player respecting maxScore`() {
        val gameSession = TestData.createGameSession()
        val currentPlayer = TestData.createFirstPlayer(points = 10)
        val affectedPlayer = TestData.createSecondPlayer(points = 20)
        val card = TestData.createStealCard()
        val turn = Turn(currentPlayer.id, gameSession, currentPlayer, affectedPlayer, card, 0)

        `when`(turnService.getLastTurn(gameSession)).thenReturn(turn)
        `when`(playerService.getCurrentSessionPlayer(gameSession)).thenReturn(currentPlayer)

        actionExecutor.executeStealAction(gameSession, 5)

        assertAll(
            { assertEquals(15, currentPlayer.points) },
            { assertEquals(15, affectedPlayer.points) }
        )
    }

    @Test
    fun `executeDoubleDownAction should double the current player's points respecting maxScore`() {
        val gameSession = TestData.createGameSession()
        val player = TestData.createFirstPlayer(points = 10)
        val card = TestData.createDoubleDownCard()
        val turn = TestData.createTurn(gameSession, player, null, card)

        `when`(playerService.getCurrentSessionPlayer(gameSession)).thenReturn(player)
        `when`(turnService.getLastTurn(gameSession)).thenReturn(turn)

        `when`(properties.maxScore).thenReturn(20)

        actionExecutor.executeDoubleDownAction(gameSession, 2)

        assertAll(
            { assertEquals(20, player.points) },
            { assertEquals(player, turn.playerUnderEffect) }
        )
    }
}