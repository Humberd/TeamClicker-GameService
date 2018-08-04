package com.teamclicker.gameservice.game.spring

import com.teamclicker.gameservice.game.lobby.Lobby
import com.teamclicker.gameservice.game.lobby.LobbyException
import com.teamclicker.gameservice.game.lobby.LobbySettings
import com.teamclicker.gameservice.models.dao.PlayerDAO
import mu.KLogging
import org.springframework.stereotype.Service

@Service
class LobbyService {
    private val lobbyMap = HashMap<Long, Lobby>()

    fun createLobby(
        settings: LobbySettings,
        host: PlayerDAO
    ): Lobby {
        val id = generateId()
        val lobby = Lobby(settings.copy(id = id), host)

        lobbyMap.put(id, lobby)
        return lobby
    }

    fun removeLobby(lobbyId: Long) {
        lobbyMap.remove(lobbyId)
    }

    fun getLobby(lobbyId: Long): Lobby {
        lobbyMap.get(lobbyId)?.let { return it }
        throw LobbyException("Lobby doesn't exist")
    }

    fun generateId() = ++idCounter

    companion object : KLogging() {
        var idCounter: Long = 0
    }
}