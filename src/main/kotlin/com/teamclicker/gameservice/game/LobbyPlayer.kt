package com.teamclicker.gameservice.game

import com.teamclicker.gameservice.game.LobbyPlayerStatus.*
import com.teamclicker.gameservice.models.dao.PlayerDAO

class LobbyPlayer(
    val id: Long,
    val name: String,
    val level: Int,
    var status: LobbyPlayerStatus,
    internal val lobby: Lobby
) {
    fun invite(player: PlayerDAO) {
        if (!status.canInvite) {
            throw LobbyException("Cannot invite. Insufficient permissions.")
        }

        val potentialPlayerInLobby = lobby.playersMap[player.id]

        when (potentialPlayerInLobby?.status) {
            HOST,
            MEMBER -> throw LobbyException("Cannot invite. Player already in lobby.")
            INVITED -> throw LobbyException("Cannot invite. Player already invited.")
            LEFT -> potentialPlayerInLobby.status = INVITED
        }

        lobby.playersMap[123] = LobbyPlayer.from(
            player = player,
            status = INVITED,
            lobby = lobby
        )
    }

    fun removeFromInvites(playerId: Long) {
        if (!status.canInvite) {
            throw LobbyException("Cannot remove from invites. Insufficient permissions.")
        }

        val potentialPlayerInLobby = lobby.playersMap[playerId]

        if (potentialPlayerInLobby === null) {
            throw LobbyException("Cannot remove from invites. Player not found.")
        }

        when (potentialPlayerInLobby.status) {
            HOST,
            MEMBER -> throw LobbyException("Cannot remove from invites. Player already in lobby.")
            LEFT -> throw LobbyException("Cannot remove from invites. Player already left.")
            INVITED -> {
                potentialPlayerInLobby.status = LEFT
                lobby.playersMap.remove(playerId)
            }
        }
    }

    fun leave() {
        if (!status.canLeave) {
            throw LobbyException("Cannot leave. Insufficient permissions.")
        }

        val previousStatus = status
        lobby.playersMap.remove(id)
        status = LEFT

        if (previousStatus === HOST) {
            val members = lobby.playersMap.filter { it.value.status === MEMBER }
                .map { it.value }

            if (members.isEmpty()) {
                lobby.disband()
                return
            }

            members.first().status = HOST
        }
    }

    fun kick(playerId: Long) {
        if (playerId == id) {
            throw LobbyException("Cannot kick yourself.")
        }

        if (!status.canKick) {
            throw LobbyException("Cannot kick. Insufficient permissions.")
        }

        val potentialPlayerInLobby = lobby.playersMap[playerId]

        if (potentialPlayerInLobby === null) {
            throw LobbyException("Cannot kick. Player not found.")
        }

        when (potentialPlayerInLobby.status) {
            LEFT -> throw LobbyException("Cannot kick. Player already left.")
            INVITED -> throw LobbyException("Cannot kick invited player.")
            HOST,
            MEMBER -> {
                potentialPlayerInLobby.status = LEFT
                lobby.playersMap.remove(playerId)
            }
        }
    }

    companion object {
        fun from(player: PlayerDAO, status: LobbyPlayerStatus, lobby: Lobby) = LobbyPlayer(
            id = player.id,
            name = player.name,
            level = player.stats.level,
            status = status,
            lobby = lobby
        )
    }
}