package com.teamclicker.gameservice.game.lobby

import com.teamclicker.gameservice.game.lobby.LobbyPlayerStatus.*
import com.teamclicker.gameservice.game.lobby.LobbyStatus.PRIVATE
import com.teamclicker.gameservice.game.lobby.LobbyStatus.PUBLIC
import com.teamclicker.gameservice.game.spring.WsLobbyAPI
import com.teamclicker.gameservice.models.dao.PlayerDAO

class Lobby(
    val id: String,
    settings: LobbySettings,
    initialHost: PlayerDAO,
    private val wsLobbyAPI: WsLobbyAPI
) {
    var isDisbanded = false
    private val settings = settings.copy()
    private val _playersMap = hashMapOf(
        initialHost.id to LobbyPlayer.from(initialHost, HOST, this)
    )

    private val players: HashMap<Long, LobbyPlayer>
        get() {
            if (isDisbanded) {
                throw LobbyException("Lobby is disbanded.")
            }
            return _playersMap
        }

    private val onDisbandListeners = ArrayList<() -> Unit>()

    fun join(player: PlayerDAO) {
        when (settings.status) {
            PRIVATE -> {
                val lobbyPlayer = getPlayer(player.id)

                /* It's a PRIVATE lobby, so only INVITED players can join */
                if (lobbyPlayer.status !== INVITED) {
                    throw LobbyException("Cannot join. Player not invited.")
                }

                lobbyPlayer.status = MEMBER
                updatePlayer(lobbyPlayer)
                return
            }
            PUBLIC -> {
                /* In a PUBLIC lobby both NOT INVITED players and INVITED can join */
                if (!hasPlayer(player.id)) {
                    addPlayer(player, MEMBER)
                    return
                }

                val lobbyPlayer = getPlayer(player.id)

                if (lobbyPlayer.status !== INVITED) {
                    throw LobbyException("Cannot join. Player already in lobby.")
                }

                lobbyPlayer.status = MEMBER
                updatePlayer(lobbyPlayer)
                return
            }
        }
    }

    fun disband() {
        if (isDisbanded) {
            throw LobbyException("Cannot disband. Lobby is already disbanded.")
        }
        isDisbanded = true
        for (listener in onDisbandListeners) {
            listener()
        }
        wsLobbyAPI.sendLobbyDisband(id)

    }

    fun onDisband(callback: () -> Unit) {
        onDisbandListeners.add(callback)
    }

    /**
     * Returns a lobby player's **CLONE**
     */
    fun getPlayer(playerId: Long): LobbyPlayer {
        players[playerId]?.let { return it.copy() }
        throw LobbyException("Player not found.")
    }

    fun getAllPlayers(): List<LobbyPlayer> {
        return players.map {
            it.value.copy()
        }
    }

    fun addPlayer(player: PlayerDAO, status: LobbyPlayerStatus): LobbyPlayer {
        return LobbyPlayer.from(
            player = player,
            status = status,
            lobby = this
        ).also {
            players[player.id] = it
            wsLobbyAPI.sendPlayerAdded(id, it)
        }
    }

    fun updatePlayer(player: LobbyPlayer) {
        players[player.id] = player.copy()
        wsLobbyAPI.sendPlayerUpdated(id, player)
    }

    fun removePlayer(playerId: Long): LobbyPlayer {
        players.remove(playerId)?.let {
            wsLobbyAPI.sendPlayerRemoved(id, playerId)
            return it
        }
        throw LobbyException("Cannot remove not existing player.")
    }

    fun findPlayers(predicate: (Map.Entry<Long, LobbyPlayer>) -> Boolean): Map<Long, LobbyPlayer> {
        return players.filter(predicate)
    }

    fun hasPlayer(playerId: Long): Boolean {
        return players.containsKey(playerId)
    }
}