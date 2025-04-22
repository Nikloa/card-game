package com.kokodi.service

import com.kokodi.entity.GameSession
import com.kokodi.entity.Turn
import com.kokodi.util.TestData
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.extension.ExtendWith

/**
 * Тест для [TurnService].
 */
@ExtendWith(MockitoExtension::class)
class TurnServiceTest {

    @InjectMocks
    private lateinit var turnService: TurnService

    @Mock
    private lateinit var gameSession: GameSession

    @Test
    fun `When creating a turn expect a valid Turn object`() {
        val firstPlayer = TestData.createFirstPlayer()
        val actionCard = TestData.createBlockCard()
        val turnsList = mutableListOf<Turn>()

        `when`(gameSession.turns).thenReturn(turnsList)

        val createdTurn = turnService.createTurn(gameSession, firstPlayer, actionCard)

        assertAll("Check created turn properties",
            { assertNotNull(createdTurn) },
            { assertEquals(firstPlayer, createdTurn.player) },
            { assertEquals(actionCard, createdTurn.card) },
            { assertEquals(1, createdTurn.step) } // Первый шаг должен быть 1.
        )
        verify(gameSession, times(2)).turns
    }

    @Test
    fun `When creating multiple turns expect correct step count`() {
        val firstPlayer = TestData.createFirstPlayer()
        val secondPlayer = TestData.createSecondPlayer()
        val firstActionCard = TestData.createBlockCard()
        val secondActionCard = TestData.createDoubleDownCard()

        val turnsList = mutableListOf<Turn>()

        `when`(gameSession.turns).thenReturn(turnsList)

        val firstTurn = turnService.createTurn(gameSession, firstPlayer, firstActionCard)
        val secondTurn = turnService.createTurn(gameSession, secondPlayer, secondActionCard)

        assertAll("Check turns count and step",
            { assertEquals(2, gameSession.turns.size) },
            { assertEquals(2, secondTurn.step) }
        )
    }

    @Test
    fun `When getting last turn expect the last created Turn`() {
        val firstPlayer = TestData.createFirstPlayer()
        val actionCard = TestData.createBlockCard()

        val turnsList = mutableListOf<Turn>()

        `when`(gameSession.turns).thenReturn(turnsList)

        val firstTurn = turnService.createTurn(gameSession, firstPlayer, actionCard)

        val lastTurn = turnService.getLastTurn(gameSession)

        assertEquals(firstTurn, lastTurn)
    }

    @Test
    fun `When updating last turn's player under effect expect updated value`() {
        val firstPlayer = TestData.createFirstPlayer()
        val secondPlayer = TestData.createSecondPlayer()

        val turnsList = mutableListOf<Turn>()

        `when`(gameSession.turns).thenReturn(turnsList)

        val firstTurn = turnService.createTurn(gameSession, firstPlayer, TestData.createBlockCard())

        turnService.updateLastTurnPlayerUnderEffect(gameSession, secondPlayer)

        assertEquals(secondPlayer, firstTurn.playerUnderEffect)
    }

    @Test
    fun `When getting last turn from empty session expect NoSuchElementException`() {
        `when`(gameSession.turns).thenReturn(mutableListOf())

        assertThrows<NoSuchElementException> {
            turnService.getLastTurn(gameSession)
        }
    }
}