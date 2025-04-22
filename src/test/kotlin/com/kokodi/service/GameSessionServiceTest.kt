package com.kokodi.service

import com.kokodi.repository.GameSessionRepository
import com.kokodi.service.util.GameStateManager
import com.kokodi.util.TestData
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.junit.jupiter.api.extension.ExtendWith
import java.util.*

@ExtendWith(MockitoExtension::class)
class GameSessionServiceTest {

    @InjectMocks
    private lateinit var gameSessionService: GameSessionService

    @Mock
    private lateinit var gameStateManager: GameStateManager

    @Mock
    private lateinit var gameSessionRepository: GameSessionRepository

    @Test
    fun `When getting a game session expect the correct session`() {

        val expectedGameSession = TestData.createGameSession()
        `when`(gameSessionRepository.findById(expectedGameSession.id!!)).thenReturn(Optional.of(expectedGameSession))

        val actualGameSession = gameSessionService.getGameSession(expectedGameSession.id!!)

        assertAll(
            { assertNotNull(actualGameSession) },
            { assertEquals(expectedGameSession.id, actualGameSession.id) }
        )
    }

    @Test
    fun `When getting a non-existing game session expect RuntimeException`() {

        val nonExistingId = TestData.FIRST_ID
        `when`(gameSessionRepository.findById(nonExistingId)).thenReturn(Optional.empty())

        val exception = assertThrows<RuntimeException> {
            gameSessionService.getGameSession(nonExistingId)
        }

        assertEquals("GameSession with id=$nonExistingId does not exist", exception.message)
    }

    @Test
    fun `When saving a game session expect saved session`() {

        val gameSessionToSave = TestData.createEmptyGameSession()

        `when`(gameSessionRepository.save(gameSessionToSave)).thenReturn(gameSessionToSave)

        val savedGameSession = gameSessionService.saveGameSession(gameSessionToSave)

        assertAll("Check saved game session properties",
            { assertNotNull(savedGameSession) },
            { assertEquals(gameSessionToSave.id, savedGameSession.id) }
        )

        verify(gameSessionRepository, times(1)).save(gameSessionToSave)
    }
}