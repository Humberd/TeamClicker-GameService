package com.teamclicker.gameservice.game

import mu.KLogging
import org.springframework.stereotype.Service

@Service
class LobbyService {
    private val lobbyMap = HashMap<Long, Lobby>()

//    fun createLobby(): Lobby {
//        val id = generateId()
//        val lobby = Lobby(id)
//
//        lobbyMap.put(id, lobby)
//        return lobby
//    }

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