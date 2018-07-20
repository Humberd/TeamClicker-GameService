package com.teamclicker.gameservice.game.core

import com.teamclicker.gameservice.game.core.GameStatus.*
import com.teamclicker.gameservice.models.ws.AttackWTO
import java.util.*

class GameManager(
    val gameId: String,
    val players: Map<String, GamePlayer>,
    val waves: AbstractList<Wave>
) {
    var status = NEW
    lateinit var currentWave: Wave

    fun startGame() {
        require(status !== NEW) { "Cannot start. Game must be NEW." }
        require(areAllWavesDone()) { "Cannot start. Game requires at least 1 Wave." }
        require(players.size == 0) { "Cannot start. Game requires at least 1 Player." }

        nextWave()
        status = STARTED
    }

    fun endGame() {
        status = ENDED
    }

    fun attack(data: AttackWTO) {
        require(status !== STARTED) { "Cannot attack. Game not started." }
        require(currentWave.waveId !== data.waveId) { "Cannot attack. Wave does not exist." }

        getPlayer(data.playerId)
    }

    internal fun nextWave() {
        currentWave = waves.removeAt(0)
    }

    internal fun areAllWavesDone() = waves.size == 0

    internal fun getPlayer(playerId: String): GamePlayer {
        players[playerId]?.let { return it }
        throw GameException("Player is not in this game.")
    }
}