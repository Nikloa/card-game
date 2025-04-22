package com.kokodi.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException::class)
    fun handleUserAlreadyExistsException(ex: UserAlreadyExistsException): ResponseEntity<String> {
        return ResponseEntity(ex.message, HttpStatus.CONFLICT)
    }

    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFoundException(ex: UserNotFoundException): ResponseEntity<String> {
        return ResponseEntity(ex.message, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(InvalidCardTypeException::class)
    fun handleInvalidCardTypeException(ex: InvalidCardTypeException): ResponseEntity<String> {
        return ResponseEntity(ex.message, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(EmptyDeckException::class)
    fun handleEmptyDeckException(ex: EmptyDeckException): ResponseEntity<String> {
        return ResponseEntity(ex.message, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(PlayerNotFoundException::class)
    fun handlePlayerNotFoundException(ex: PlayerNotFoundException): ResponseEntity<String> {
        return ResponseEntity(ex.message, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(PlayerNotActiveException::class)
    fun handlePlayerNotActiveException(ex: PlayerNotActiveException): ResponseEntity<String> {
        return ResponseEntity(ex.message, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(ThereNoActivePlayersException::class)
    fun handleThereNoActivePlayersException(ex: ThereNoActivePlayersException): ResponseEntity<String> {
        return ResponseEntity(ex.message, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(NotEnoughPlayersException::class)
    fun handleNotEnoughPlayersException(ex: NotEnoughPlayersException): ResponseEntity<String> {
        return ResponseEntity(ex.message, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(TooManyPlayersException::class)
    fun handleTooManyPlayersException(ex: TooManyPlayersException): ResponseEntity<String> {
        return ResponseEntity(ex.message, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(GameNotWaitingForPlayersException::class)
    fun handleGameNotWaitingForPlayersException(ex: GameNotWaitingForPlayersException): ResponseEntity<String> {
        return ResponseEntity(ex.message, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(GameNotInProgressException::class)
    fun handleGameNotInProgressException(ex: GameNotInProgressException): ResponseEntity<String> {
        return ResponseEntity(ex.message, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(SamePlayerException::class)
    fun handleSamePlayerException(ex: SamePlayerException): ResponseEntity<String> {
        return ResponseEntity(ex.message, HttpStatus.BAD_REQUEST)
    }
}

class UserAlreadyExistsException(message: String) : RuntimeException(message)

class UserNotFoundException(message: String) : RuntimeException(message)

class InvalidCardTypeException(message: String) : RuntimeException(message)

class EmptyDeckException(message: String) : RuntimeException(message)

class PlayerNotFoundException(message: String) : RuntimeException(message)

class PlayerNotActiveException(message: String) : RuntimeException(message)

class ThereNoActivePlayersException(message: String) : RuntimeException(message)

class NotEnoughPlayersException(message: String) : RuntimeException(message)

class TooManyPlayersException(message: String) : RuntimeException(message)

class GameNotWaitingForPlayersException(message: String) : RuntimeException(message)

class GameNotInProgressException(message: String) : RuntimeException(message)

class SamePlayerException(message: String) : RuntimeException(message)