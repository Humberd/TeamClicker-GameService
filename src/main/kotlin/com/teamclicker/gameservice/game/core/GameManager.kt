package com.teamclicker.gameservice.game.core

import com.teamclicker.gameservice.game.core.GameStatus.*
import com.teamclicker.gameservice.models.ws.AttackWTO

class GameManager(
    val gameId: String,
    val players: Map<Long, GamePlayer>,
    val waves: MutableList<Wave>,
    val eventReceiver: EventReceiver
) {
    val actions = Actions()
    var status = NEW
    lateinit var currentWave: Wave

    fun startGame() {
        check(status === NEW) { "Cannot start. Game must be NEW." }
        check(!waves.isEmpty()) { "Cannot start. Game requires at least 1 Wave." }
        check(!players.isEmpty()) { "Cannot start. Game requires at least 1 Player." }

        nextWave()
        status = STARTED
    }

    fun endGame() {
        status = ENDED
    }

    internal fun nextWave() {
        currentWave = waves.removeAt(0)
    }

    internal fun getPlayer(playerId: Long): GamePlayer {
        players[playerId]?.let { return it }
        throw GameException("Player is not in this game.")
    }

    inner class Actions {
        fun attack(data: AttackWTO) {
            check(status === STARTED) { "Cannot attack. Game not started." }
            require(currentWave.waveId === data.waveId) { "Cannot attack. Wave does not exist." }

            val player = getPlayer(data.playerId)
            val creature = currentWave.getCreature(data.creatureId)

            eventReceiver.queueTask {
                creature.template.hp -= player.stats.atk
            }
        }
    }
}