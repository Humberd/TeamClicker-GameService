package com.teamclicker.gameservice.game.lobby

import com.teamclicker.gameservice.game.lobby.LobbyPlayerStatus.*
import com.teamclicker.gameservice.game.lobby.LobbyStatus.PRIVATE
import com.teamclicker.gameservice.game.lobby.LobbyStatus.PUBLIC
import com.teamclicker.gameservice.models.dao.PlayerDAO

class Lobby(
    val id: String,
    settings: LobbySettings,
    initialHost: PlayerDAO
) {
    internal val settings = settings.copy()
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
    var isDisbanded = false

    fun disband() {
        isDisbanded = true
    }

    fun join(player: PlayerDAO) {
        val potentialLobbyPlayer = getPlayer(player.id)

        when (settings.status) {
            PRIVATE -> {
                if (potentialLobbyPlayer.status !== INVITED) {
                    throw LobbyException("Cannot join. Player not invited.")
                }

                potentialLobbyPlayer.status = MEMBER
            }
            PUBLIC -> {
                when (potentialLobbyPlayer.status) {
                    HOST,
                    MEMBER -> throw LobbyException("Cannot join. Player already in the lobby.")
                    LEFT,
                    INVITED -> {
                        potentialLobbyPlayer.status = MEMBER
                    }
                }
            }
        }

        addPlayer(player, MEMBER)
    }

    fun getPlayer(playerId: Long): LobbyPlayer {
        players[playerId]?.let { return it }
        throw LobbyException("Player not found.")
    }

    fun removePlayer(playerId: Long) {
        if (players.remove(playerId) === null) {
            throw LobbyException("Cannot remove not existing player.")
        }
    }

    fun findPlayers(predicate: (Map.Entry<Long, LobbyPlayer>) -> Boolean) = players.filter(predicate)

    internal fun addPlayer(player: PlayerDAO, status: LobbyPlayerStatus): LobbyPlayer {
        return LobbyPlayer.from(
            player = player,
            status = status,
            lobby = this
        ).also {
            players[player.id] = it
        }
    }
}