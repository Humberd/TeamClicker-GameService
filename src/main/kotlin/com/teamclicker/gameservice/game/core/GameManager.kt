package com.teamclicker.gameservice.game.core

import com.teamclicker.gameservice.game.core.GameStatus.*
import java.util.*

class GameManager(
    val gameId: Long,
    val players: Map<Long, GamePlayer>,
    val waves: AbstractList<Wave>
) {
    var status = NEW
    lateinit var currentWave: Wave

    fun startGame() {
        if (status !== NEW) {
            throw GameException("Cannot start. Game must be NEW.")
        }
        if (areAllWavesDone()) {
            throw GameException("Cannot start. Game requires at least 1 Wave.")
        }
        if (players.size == 0) {
            throw GameException("Cannot start. Game requires at least 1 Player.")
        }
        nextWave()
        status = STARTED
    }

    fun endGame() {
        status = ENDED
    }

    fun attack(playerId: Long, waveId: String) {
        if (status !== STARTED) {
            throw GameException("Cannot attack. Game not started.")
        }
        if (currentWave.id !== waveId) {
            throw GameException("Cannot attack. Wave does not exist.")
        }
        getPlayer(playerId)
    }

    internal fun nextWave() {
        currentWave = waves.removeAt(0)
    }

    internal fun areAllWavesDone() = waves.size == 0

    internal fun getPlayer(id: Long): GamePlayer {
        players[id]?.let { return it }
        throw GameException("Player is not in this game.")
    }
}