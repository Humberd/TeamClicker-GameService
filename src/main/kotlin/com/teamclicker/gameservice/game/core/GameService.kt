package com.teamclicker.gameservice.game.core

import mu.KLogging
import org.springframework.stereotype.Service

@Service
class GameService {
    private val gamesMap = HashMap<Long, GameManager>()

//    fun createGame(): GameManager {
//        return GameManager(generateId())
//    }

    fun removeGame(gameId: Long) {
        gamesMap.remove(gameId)
    }

    fun getGame(gameId: Long): GameManager {
        gamesMap.get(gameId)?.let { return it }
        throw GameException("Game doesn't exist")
    }

    fun generateId() = ++idCounter

    companion object : KLogging() {
        var idCounter: Long = 0
    }
}