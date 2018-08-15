package com.teamclicker.gameservice.game.spring

import com.teamclicker.gameservice.game.lobby.Lobby
import com.teamclicker.gameservice.game.lobby.LobbyException
import com.teamclicker.gameservice.game.lobby.LobbySettings
import com.teamclicker.gameservice.models.dao.PlayerDAO
import com.teamclicker.gameservice.models.dto.LobbyCreateDTO
import com.teamclicker.gameservice.utils.Generators
import mu.KLogging
import org.springframework.stereotype.Service

@Service
class LobbyService {
    private val lobbyMap = HashMap<String, Lobby>()

    fun create(
        settings: LobbyCreateDTO,
        host: PlayerDAO
    ): Lobby {
        val lobby = Lobby(
            id = Generators.lobbyId(),
            settings = LobbySettings(
                status = settings.status
            ),
            initialHost = host
        )

        lobbyMap.put(lobby.id, lobby)
        return lobby
    }

    fun remove(lobbyId: String) {
        lobbyMap.remove(lobbyId)
    }

    fun get(lobbyId: String): Lobby {
        lobbyMap.get(lobbyId)?.let { return it }
        throw LobbyException("Lobby doesn't exist")
    }

    companion object : KLogging()
}