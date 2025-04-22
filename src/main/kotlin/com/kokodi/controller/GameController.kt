package com.kokodi.controller

import com.kokodi.dto.GameDto
import com.kokodi.dto.TurnDto
import com.kokodi.facade.GameFacade
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/games")
class GameController(private val gameFacade: GameFacade) {

    @PostMapping("/new")
    fun createGameSession(): ResponseEntity<GameDto> {
        val gameSession = gameFacade.createGameSession()
        return ResponseEntity(gameSession, HttpStatus.CREATED)
    }

    @PostMapping("/{gameId}/join")
    fun joinGameSession(@PathVariable gameId: Long): ResponseEntity<GameDto> {
        val gameSession = gameFacade.joinGameSession(gameId)
        return ResponseEntity(gameSession, HttpStatus.OK)
    }

    @PostMapping("/{gameId}/start")
    fun startGameSession(@PathVariable gameId: Long): ResponseEntity<GameDto> {
        val gameSession = gameFacade.startGameSession(gameId)
        return ResponseEntity(gameSession, HttpStatus.OK)
    }

    @PostMapping("/{gameId}/turn")
    fun stepGameSession(@PathVariable gameId: Long): ResponseEntity<GameDto> {
        val gameSession = gameFacade.stepGameSession(gameId)
        return ResponseEntity(gameSession, HttpStatus.OK)
    }

    @PostMapping("/{gameId}/turn/{playerId}")
    fun stealAction(@PathVariable gameId: Long, @PathVariable playerId: Long): ResponseEntity<GameDto> {
        val gameSession = gameFacade.stealAction(gameId, playerId)
        return ResponseEntity(gameSession, HttpStatus.OK)
    }

    @GetMapping("/{gameId}/history")
    fun history(@PathVariable gameId: Long): ResponseEntity<List<TurnDto>> {
        val history = gameFacade.getHistory(gameId)
        return ResponseEntity(history, HttpStatus.OK)
    }
}