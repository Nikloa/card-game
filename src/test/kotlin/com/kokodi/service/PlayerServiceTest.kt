package com.kokodi.service

import com.kokodi.entity.GameSession
import com.kokodi.entity.Player
import com.kokodi.exception.PlayerNotActiveException
import com.kokodi.exception.PlayerNotFoundException
import com.kokodi.exception.ThereNoActivePlayersException
import com.kokodi.util.TestData
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.Authentication

@ExtendWith(MockitoExtension::class)
class PlayerServiceTest {

    @InjectMocks
    private lateinit var playerService: PlayerService

    @Mock
    private lateinit var gameSession: GameSession

    @Mock
    private lateinit var authentication: Authentication

    @Mock
    private lateinit var userService: UserService

    @Test
    fun `When getting current player expect valid Player object`() {
        val firstUser = TestData.createFirstUser()
        SecurityContextHolder.getContext().authentication = authentication
        `when`(authentication.name).thenReturn(firstUser.login)
        `when`(userService.getByLogin(firstUser.login)).thenReturn(firstUser)

        val currentPlayer = playerService.getCurrentPlayer()

        assertAll("Check current player properties",
            { assertNotNull(currentPlayer) },
            { assertEquals(firstUser, currentPlayer.user) }
        )
    }

    @Test
    fun `When validating player's turn and it's not their turn expect PlayerNotActiveException`() {
        val firstUser = TestData.createFirstUser()
        val secondUser = TestData.createSecondUser()
        val activePlayer = Player(user = secondUser)

        SecurityContextHolder.getContext().authentication = authentication
        `when`(authentication.name).thenReturn(firstUser.login)
        `when`(userService.getByLogin(firstUser.login)).thenReturn(firstUser)

        val exception = assertThrows<PlayerNotActiveException> {
            playerService.validatePlayerTurn(activePlayer)
        }

        assertEquals("It's not your turn, ${firstUser.name}.", exception.message)
    }

    @Test
    fun `When validating player's turn and it is their turn expect no exception`() {
        val firstUser = TestData.createFirstUser()
        val activePlayer = Player(user = firstUser)

        SecurityContextHolder.getContext().authentication = authentication
        `when`(authentication.name).thenReturn(firstUser.login)
        `when`(userService.getByLogin(firstUser.login)).thenReturn(firstUser)

        assertDoesNotThrow {
            playerService.validatePlayerTurn(activePlayer)
        }
    }

    @Test
    fun `When getting active player expect valid Player object`() {
        val playerNotActive = TestData.createFirstPlayer(isPlayerTurn = false)
        val playerActive = TestData.createSecondPlayer(isPlayerTurn = true)

        `when`(gameSession.players).thenReturn(mutableSetOf(playerNotActive, playerActive))

        val activePlayer = playerService.getActivePlayer(gameSession)

        assertEquals(playerActive, activePlayer)
    }

    @Test
    fun `When getting active player and none found expect ThereNoActivePlayersException`() {
        val playerNotActive = TestData.createFirstPlayer(isPlayerTurn = false)

        `when`(gameSession.players).thenReturn(mutableSetOf(playerNotActive))

        val exception = assertThrows<ThereNoActivePlayersException> {
            playerService.getActivePlayer(gameSession)
        }

        assertEquals("No active player found.", exception.message)
    }

    @Test
    fun `When setting next player's turn expect correct reassignment`() {
        val playerCurrentlyActive = TestData.createFirstPlayer(turnOrder = 0, isPlayerTurn = true)
        val playerNextInLine = TestData.createSecondPlayer(turnOrder = 1, isPlayerTurn = false)

        `when`(gameSession.players).thenReturn(mutableSetOf(playerCurrentlyActive, playerNextInLine))

        playerService.setNextPlayerTurn(gameSession, playerCurrentlyActive)

        assertAll("Check players' turn reassignment",
            { assertFalse(playerCurrentlyActive.isPlayerTurn) },
            { assertTrue(playerNextInLine.isPlayerTurn) }
        )
    }

    @Test
    fun `When getting a player with session expect valid Player object`() {
        val gameSessionMocked: GameSession = mock(GameSession::class.java)

        val existingPlayerId: Long = TestData.FIRST_ID

        val existingPlayer: Player = TestData.createFirstPlayer()

        `when`(gameSessionMocked.players).thenReturn(mutableSetOf(existingPlayer))

        val retrievedPlayer = playerService.getPlayerWithSession(existingPlayerId, gameSessionMocked)

        assertEquals(existingPlayer, retrievedPlayer)
    }

    @Test
    fun `When getting a non-existing player with session expect PlayerNotFoundException`() {
        val gameSessionMocked: GameSession = mock(GameSession::class.java)

        `when`(gameSessionMocked.players).thenReturn(mutableSetOf())

        val exception = assertThrows<PlayerNotFoundException> {
            playerService.getPlayerWithSession(TestData.FIRST_ID, gameSessionMocked)
        }

        assertEquals("Player with id=${TestData.FIRST_ID} does not exist in game", exception.message)
    }
}