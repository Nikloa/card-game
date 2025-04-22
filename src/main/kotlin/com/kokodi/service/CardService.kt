package com.kokodi.service

import com.kokodi.entity.GameSession
import com.kokodi.entity.cards.ActionCard
import com.kokodi.entity.cards.Card
import com.kokodi.entity.cards.DeckCard
import com.kokodi.exception.EmptyDeckException
import com.kokodi.exception.InvalidCardTypeException
import com.kokodi.repository.CardRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class CardService(
    private val cardRepository: CardRepository
) {

    fun createDeck(): MutableList<DeckCard> {
        val cards: List<Card> = cardRepository.findAll().shuffled()
        val deck: MutableList<DeckCard> = mutableListOf()
        cards.forEachIndexed { index, card ->
            val deckCard = DeckCard(card = card, position = index)
            deck.add(deckCard)
        }
        return deck
    }

    fun getCurrentDeckCard(gameSession: GameSession): DeckCard {
        return gameSession.deck.removeLastOrNull() ?: throw EmptyDeckException("No cards left in the deck.")
    }

    fun getLastCard(gameSession: GameSession): Card {
        return gameSession.turns.last().card
    }

    fun validateCardTypeSteal(card: Card) {
        if (card !is ActionCard || card.type != ActionCard.Type.STEAL) {
            throw InvalidCardTypeException("Expected a Steal card but got ${card::class.simpleName}.")
        }
    }
}