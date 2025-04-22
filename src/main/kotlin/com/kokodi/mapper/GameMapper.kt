package com.kokodi.mapper

import com.kokodi.dto.CardDto
import com.kokodi.dto.GameDto
import com.kokodi.dto.PlayerDto
import com.kokodi.dto.TurnDto
import com.kokodi.entity.GameSession
import com.kokodi.entity.Player
import com.kokodi.entity.Turn
import com.kokodi.entity.cards.ActionCard
import com.kokodi.entity.cards.Card
import org.springframework.stereotype.Component

@Component
object GameMapper {

    fun toGameDto(gameSession: GameSession): GameDto {
        return GameDto(
            gameId = gameSession.id?.toString() ?: "Неизвестный id",
            players = gameSession.players.map { toPlayerDto(it) },
            cardsLeft = gameSession.deck.size.toString(),
            lastTurn = getLastTurnDescription(gameSession)
        )
    }

    private fun toPlayerDto(player: Player): PlayerDto {
        return PlayerDto(
            playerId = player.id?.toString() ?: "Неизвестный id",
            name = player.user.name,
            points = player.points.toString(),
            hisTurn = player.isPlayerTurn
        )
    }

    private fun getLastTurnDescription(gameSession: GameSession): String {
        val lastTurn = gameSession.turns.lastOrNull()
        return if (gameSession.state == GameSession.State.WAIT_FOR_PLAYERS) {
            "Игра в ожидании игроков"
        } else if (gameSession.state == GameSession.State.FINISHED) {
            "Игра завершена"
        } else if (lastTurn != null) {
            "Игрок ${lastTurn.player.user.name} вытянул карту ${lastTurn.card.name}. " +
                    "Игрок ${lastTurn.playerUnderEffect?.user?.name ?: "выберите игрока который"} ${lastTurn.card.description}"
        } else {
            "Еще не было ходов"
        }
    }

    fun toTurnDto(turn: Turn): TurnDto {
        return TurnDto(
            card = toCardDto(turn.card),
            player = toPlayerDto(turn.player),
            playerUnderEffect = turn.playerUnderEffect?.let { toPlayerDto(it) },
            step = turn.step.toString()
        )
    }

    private fun toCardDto(card: Card): CardDto {
        return CardDto(
            name = card.name,
            value = card.value.toString(),
            description = card.description,
            type = (card as? ActionCard)?.type?.name ?: ""
        )
    }
}