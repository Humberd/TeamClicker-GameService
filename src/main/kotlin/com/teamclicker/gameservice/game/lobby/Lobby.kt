package com.teamclicker.gameservice.game.lobby

import com.teamclicker.gameservice.game.lobby.LobbyPlayerStatus.*
import com.teamclicker.gameservice.game.lobby.LobbyStatus.PRIVATE
import com.teamclicker.gameservice.game.lobby.LobbyStatus.PUBLIC
import com.teamclicker.gameservice.models.dao.PlayerDAO

class Lobby(
    settings: LobbySettings,
    initialHost: PlayerDAO
) {
    internal val settings = settings.copy()
    private val _playersMap = hashMapOf(
        initialHost.id to LobbyPlayer.from(initialHost, HOST, this)
    )

    val playersMap: HashMap<Long, LobbyPlayer>
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
        val potentialLobbyPlayer = playersMap[player.id]

        when (settings.status) {
            PRIVATE -> {
                if (potentialLobbyPlayer === null) {
                    throw LobbyException("Cannot join. Player not found")
                }

                if (potentialLobbyPlayer.status !== INVITED) {
                    throw LobbyException("Cannot join. Player not invited.")
                }

                potentialLobbyPlayer.status = MEMBER
            }
            PUBLIC -> {
                when (potentialLobbyPlayer?.status) {
                    HOST,
                    MEMBER -> throw LobbyException("Cannot join. Player already in the lobby.")
                    LEFT,
                    INVITED -> {
                        potentialLobbyPlayer.status = MEMBER
                        return
                    }
                }

                addPlayer(player, MEMBER)
            }
        }
    }

    internal fun addPlayer(player: PlayerDAO, status: LobbyPlayerStatus) {
        playersMap[player.id] = LobbyPlayer.from(
            player = player,
            status = status,
            lobby = this
        )
    }
}