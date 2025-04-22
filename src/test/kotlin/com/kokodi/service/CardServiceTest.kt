package com.kokodi.service

import com.kokodi.exception.InvalidCardTypeException
import com.kokodi.exception.EmptyDeckException
import com.kokodi.repository.CardRepository
import com.kokodi.util.TestData
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.junit.jupiter.api.extension.ExtendWith

/**
 * Тест для [CardService].
 */
@ExtendWith(MockitoExtension::class)
class CardServiceTest {

    @InjectMocks
    private lateinit var cardService: CardService

    @Mock
    private lateinit var cardRepository: CardRepository

    @Test
    fun `When creating a deck expect a valid Queue of DeckCards`() {

        val expectedDeck = TestData.createDeck()
        `when`(cardRepository.findAll()).thenReturn(expectedDeck.map { it.card })

        val actualDeck = cardService.createDeck()

        assertAll("Check created deck properties",
            { assertNotNull(actualDeck) },
            { assertEquals(expectedDeck.size, actualDeck.size) }
        )
        verify(cardRepository, times(1)).findAll()
    }

    @Test
    fun `When getting current deck card expect the top card from the deck`() {

        val gameSession = TestData.createGameSession()
        val deckSize = gameSession.deck.size

        val currentCard = cardService.getCurrentDeckCard(gameSession)

        assertAll(
            { assertNotNull(currentCard) },
            { assertEquals(deckSize - 1, gameSession.deck.size) }
        )
    }

    @Test
    fun `When getting current deck card from empty deck expect EmptyDeckException`() {

        val emptyGameSession = TestData.createEmptyGameSession()

        assertThrows<EmptyDeckException> {
            cardService.getCurrentDeckCard(emptyGameSession)
        }
    }

    @Test
    fun `When getting last card expect the last played card from turns`() {

        val gameSession = TestData.createTurnGameSession()

        val lastCard = cardService.getLastCard(gameSession)

        assertEquals(gameSession.turns.last().card, lastCard)
    }

    @Test
    fun `When validating a steal action card expect no exception for valid card`() {

        val stealCard = TestData.createStealCard()


        assertDoesNotThrow {
            cardService.validateCardTypeSteal(stealCard)
        }
    }

    @Test
    fun `When validating a non-steal action card expect InvalidCardTypeException`() {
        val blockCard = TestData.createBlockCard()

        assertThrows<InvalidCardTypeException> {
            cardService.validateCardTypeSteal(blockCard)
        }
    }
}