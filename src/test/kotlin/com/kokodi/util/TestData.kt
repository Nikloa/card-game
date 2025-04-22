package com.kokodi.util

import com.kokodi.entity.GameSession
import com.kokodi.entity.Player
import com.kokodi.entity.Turn
import com.kokodi.entity.User
import com.kokodi.entity.cards.ActionCard
import com.kokodi.entity.cards.Card
import com.kokodi.entity.cards.DeckCard
import com.kokodi.entity.cards.PointCard
import java.util.*

object TestData {

    const val FIRST_LOGIN = "FIRST_LOGIN"
    const val SECOND_LOGIN = "SECOND_LOGIN"
    const val THIRD_LOGIN = "THIRD_LOGIN"
    const val FOURTH_LOGIN = "FOURTH_LOGIN"
    const val PASSWORD = "PASSWORD"
    const val FIRST_NAME = "FIRST_NAME"
    const val SECOND_NAME = "SECOND_NAME"
    const val THIRD_NAME = "THIRD_NAME"
    const val FOURTH_NAME = "FOURTH_NAME"
    val FIRST_ID = Random().nextLong()
    val SECOND_ID = Random().nextLong()
    val THIRD_ID = Random().nextLong()
    val FOURTH_ID = Random().nextLong()
    const val FIRST_VALUE = 1
    const val SECOND_VALUE = 2
    const val THIRD_VALUE = 3
    const val FOURTH_VALUE = 4
    const val FIRST_TURN_ORDER = 1
    const val SECOND_TURN_ORDER = 2
    const val THIRD_TURN_ORDER = 3
    const val FOURTH_TURN_ORDER = 4
    const val ANY_DESCRIPTION = "ANY_DESCRIPTION"

    fun createGameSession(
        state: GameSession.State = GameSession.State.IN_PROGRESS
    ) = GameSession(
        FIRST_ID,
        mutableSetOf(createFirstPlayer(), createSecondPlayer()),
        state,
        createDeck()
    )

    fun createTurnGameSession(
        state: GameSession.State = GameSession.State.IN_PROGRESS
    ) = GameSession(
        FIRST_ID,
        mutableSetOf(createFirstPlayer(), createSecondPlayer()),
        state,
        createDeck(),
        mutableListOf(
            createFirstTurn(),
            createSecondTurn(),
            createThirdTurn(),
            createFourthTurn()
        )
    )

    fun createEmptyGameSession() = GameSession(
        FIRST_ID,
        mutableSetOf(),
        GameSession.State.IN_PROGRESS,
        LinkedList()
    )

    fun createFirstPlayer(
        turnOrder: Int = FIRST_TURN_ORDER,
        points: Int = 15,
        isPlayerTurn: Boolean = false
    ) = Player(
        FIRST_ID,
        createFirstUser(),
        null,
        points,
        turnOrder,
        isPlayerTurn
    )

    fun createSecondPlayer(
        turnOrder: Int = SECOND_TURN_ORDER,
        points: Int = 15,
        isPlayerTurn: Boolean = true
    ) = Player(
        SECOND_ID,
        createSecondUser(),
        null,
        points,
        turnOrder,
        isPlayerTurn
    )

    fun createThirdPlayer(
        turnOrder: Int = THIRD_TURN_ORDER,
        points: Int = 15,
        isPlayerTurn: Boolean = false
    ) = Player(
        THIRD_ID,
        createThirdUser(),
        null,
        points,
        turnOrder,
        isPlayerTurn
    )

    fun createFourthPlayer(
        turnOrder: Int = FOURTH_TURN_ORDER,
        points: Int = 15,
        isPlayerTurn: Boolean = false
    ) = Player(
        FOURTH_ID,
        createFourthUser(),
        null,
        points,
        turnOrder,
        isPlayerTurn
    )


    // Создание пользователей для третьего и четвертого игроков.
    fun createThirdUser() = User(
        THIRD_ID,
        THIRD_NAME,
        THIRD_LOGIN,
        PASSWORD
    )

    fun createFourthUser() = User(
        FOURTH_ID,
        FOURTH_NAME,
        FOURTH_LOGIN,
        PASSWORD
    )

    fun createFirstUser() = User(
        FIRST_ID,
        FIRST_NAME,
        FIRST_LOGIN,
        PASSWORD
    )

    fun createSecondUser() = User(
        SECOND_ID,
        SECOND_NAME,
        SECOND_LOGIN,
        PASSWORD
    )

    fun createDeck(
        firstPosition: Int = 1,
        secondPosition: Int = 2,
        thirdPosition: Int = 3,
        fourthPosition: Int = 4
    ): MutableList<DeckCard> {
        val deck = LinkedList<DeckCard>()
        deck.addAll(mutableListOf(
            createFirstDeckCard(firstPosition),
            createSecondDeckCard(secondPosition),
            createThirdDeckCard(thirdPosition),
            createFourthDeckCard(fourthPosition)
        ))
        return deck
    }

    fun createFirstDeckCard(position: Int) = DeckCard(
        FIRST_ID,
        createBlockCard(),
        null,
        position
    )

    fun createSecondDeckCard(position: Int) = DeckCard(
        SECOND_ID,
        createDoubleDownCard(),
        null,
        position
    )

    fun createThirdDeckCard(position: Int) = DeckCard(
        THIRD_ID,
        createStealCard(),
        null,
        position
    )

    fun createFourthDeckCard(position: Int) = DeckCard(
        FOURTH_ID,
        createPointCard(),
        null,
        position
    )

    fun createBlockCard() = ActionCard(
        FIRST_ID,
        FIRST_NAME,
        FIRST_VALUE,
        ANY_DESCRIPTION,
        ActionCard.Type.BLOCK
    )

    fun createDoubleDownCard() = ActionCard(
        SECOND_ID,
        SECOND_NAME,
        SECOND_VALUE,
        ANY_DESCRIPTION,
        ActionCard.Type.DOUBLE_DOWN
    )

    fun createStealCard() = ActionCard(
        THIRD_ID,
        THIRD_NAME,
        THIRD_VALUE,
        ANY_DESCRIPTION,
        ActionCard.Type.STEAL
    )

    fun createPointCard() = PointCard(
        FOURTH_ID,
        FOURTH_NAME,
        FOURTH_VALUE,
        ANY_DESCRIPTION
    )

    fun createFirstTurn() = Turn(
        FIRST_ID,
        createGameSession(),
        createFirstPlayer(),
        createSecondPlayer(),
        createBlockCard(),
        FIRST_VALUE
    )

    fun createSecondTurn() = Turn(
        SECOND_ID,
        createGameSession(),
        createFirstPlayer(),
        createFirstPlayer(),
        createDoubleDownCard(),
        SECOND_VALUE
    )

    fun createThirdTurn() = Turn(
        THIRD_ID,
        createGameSession(),
        createSecondPlayer(),
        createFirstPlayer(),
        createStealCard(),
        THIRD_VALUE
    )

    fun createFourthTurn() = Turn(
        FOURTH_ID,
        createGameSession(),
        createFirstPlayer(),
        createFirstPlayer(),
        createPointCard(),
        FOURTH_VALUE
    )

    fun createTurn(
        gameSession: GameSession,
        player: Player,
        playerUnderEffect: Player?,
        card: Card
    ) = Turn(FIRST_ID, gameSession, player, playerUnderEffect, card, 0)
}