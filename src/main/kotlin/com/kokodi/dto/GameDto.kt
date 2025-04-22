package com.kokodi.dto

data class GameDto(
    val gameId: String,
    val players: List<PlayerDto>,
    val cardsLeft: String,
    val lastTurn: String
)