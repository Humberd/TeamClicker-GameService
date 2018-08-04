package com.teamclicker.gameservice.game.spring

import com.teamclicker.gameservice.game.core.GameException
import com.teamclicker.gameservice.game.core.GameManager
import com.teamclicker.gameservice.game.core.GamePlayer
import com.teamclicker.gameservice.game.core.Wave
import com.teamclicker.gameservice.game.workers.WorkersSupervisor
import com.teamclicker.gameservice.models.dao.PlayerDAO
import com.teamclicker.gameservice.game.templates.WaveTemplate
import com.teamclicker.gameservice.utils.Generators
import mu.KLogging
import org.springframework.stereotype.Service

@Service
class GameService(val workersSupervisor: WorkersSupervisor) {
    private val gamesMap = HashMap<String, GameManager>()

    fun createGame(
        players: List<PlayerDAO>,
        waveTemplates: List<WaveTemplate>
    ): GameManager {
        val gamePlayersList = players.map { Pair(it.id, GamePlayer(it.id, it.stats)) }
        val gamePlayersMap = mapOf(*gamePlayersList.toTypedArray())

        val manager = GameManager(
            gameId = Generators.gameId(),
            players = gamePlayersMap,
            waves = waveTemplates.map {
                Wave(
                    waveId = Generators.waveId(),
                    template = it
                )
            }.toMutableList(),
            eventReceiver = workersSupervisor.getEventReceiver()
        )
        gamesMap.put(manager.gameId, manager)
        return manager
    }

    fun removeGame(gameId: String) {
        gamesMap.remove(gameId)?.let { return }
        throw GameException("Game $gameId doesn't exist")
    }

    fun getGame(gameId: String): GameManager {
        gamesMap.get(gameId)?.let { return it }
        throw GameException("Game $gameId doesn't exist")
    }

    companion object : KLogging()
}