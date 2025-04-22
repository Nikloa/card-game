package com.kokodi.dto

data class TurnDto(
    val card: CardDto,
    val player: PlayerDto,
    val playerUnderEffect: PlayerDto?,
    val step: String
)