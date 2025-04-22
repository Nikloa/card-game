package com.kokodi.service.util

import com.kokodi.entity.GameSession
import com.kokodi.exception.GameNotInProgressException
import com.kokodi.exception.GameNotWaitingForPlayersException
import com.kokodi.exception.NotEnoughPlayersException
import com.kokodi.exception.TooManyPlayersException
import com.kokodi.util.TestData
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.mockito.InjectMocks
import org.mockito.junit.jupiter.MockitoExtension
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito

@ExtendWith(MockitoExtension::class)
class GameStateManagerTest {

    @Mock
    private lateinit var gameProperties: GameProperties

    @InjectMocks
    private lateinit var gameStateManager: GameStateManager

    @Test
    fun `When checking for minimum players and players are less than minimum expect NotEnoughPlayersException`() {

        Mockito.`when`(gameProperties.minPlayers).thenReturn(2)

        val session = TestData.createEmptyGameSession()
        session.players.add(TestData.createFirstPlayer())

        assertThrows<NotEnoughPlayersException> {
            gameStateManager.checkForPlayersMinSize(session)
        }
    }

    @Test
    fun `When checking for minimum players and players are enough expect no exception`() {

        Mockito.`when`(gameProperties.minPlayers).thenReturn(2)

        val session = TestData.createEmptyGameSession()
        session.players.add(TestData.createFirstPlayer())
        session.players.add(TestData.createSecondPlayer())

        assertDoesNotThrow {
            gameStateManager.checkForPlayersMinSize(session)
        }
    }

    @Test
    fun `When checking for maximum players and players are at maximum expect TooManyPlayersException`() {

        Mockito.`when`(gameProperties.maxPlayers).thenReturn(4)

        val session = TestData.createEmptyGameSession()
        session.players.add(TestData.createFirstPlayer())
        session.players.add(TestData.createSecondPlayer())
        session.players.add(TestData.createThirdPlayer())
        session.players.add(TestData.createFourthPlayer())

        assertThrows<TooManyPlayersException> {
            gameStateManager.checkForPlayersMaxSize(session)
        }
    }

    @Test
    fun `When checking for maximum players and players are less than maximum expect no exception`() {

        Mockito.`when`(gameProperties.maxPlayers).thenReturn(4)

        val session = TestData.createEmptyGameSession()
        session.players.add(TestData.createFirstPlayer())
        session.players.add(TestData.createSecondPlayer())

        assertDoesNotThrow {
            gameStateManager.checkForPlayersMaxSize(session)
        }
    }

    @Test
    fun `When validating game state is WAIT_FOR_PLAYERS expect no exception`() {
        val session = TestData.createGameSession(GameSession.State.WAIT_FOR_PLAYERS)

        assertDoesNotThrow {
            gameStateManager.validateGameWaitForPlayers(session)
        }
    }

    @Test
    fun `When validating game state is not WAIT_FOR_PLAYERS expect GameNotWaitingForPlayersException`() {
        val session = TestData.createGameSession(GameSession.State.IN_PROGRESS)

        assertThrows<GameNotWaitingForPlayersException> {
            gameStateManager.validateGameWaitForPlayers(session)
        }
    }

    @Test
    fun `When validating game in progress expect no exception`() {
        val session = TestData.createGameSession(GameSession.State.IN_PROGRESS)

        assertDoesNotThrow {
            gameStateManager.validateGameInProgress(session)
        }
    }

    @Test
    fun `When validating game not in progress expect GameNotInProgressException`() {
        val session = TestData.createGameSession(GameSession.State.FINISHED)

        assertThrows<GameNotInProgressException> {
            gameStateManager.validateGameInProgress(session)
        }
    }

    @Test
    fun `When validating win condition and win condition met expect finishGame to be called`() {

        Mockito.`when`(gameProperties.maxScore).thenReturn(30)

        val playerWithWinningScore = TestData.createFirstPlayer(points = 30)

        val session = TestData.createEmptyGameSession()

        session.players.add(playerWithWinningScore)

        val spy = Mockito.spy(gameStateManager)

        spy.validateWinCondition(session)

        Mockito.verify(spy).finishGame(session)
    }
}