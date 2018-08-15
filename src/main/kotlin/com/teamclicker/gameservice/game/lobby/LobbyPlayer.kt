package com.teamclicker.gameservice.game.lobby

import com.teamclicker.gameservice.game.lobby.LobbyPlayerStatus.*
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
            throw LobbyException("Cannot invite. Insufficient permissions. $status")
        }

        val potentialPlayerInLobby = lobby.getPlayer(player.id)

        when (potentialPlayerInLobby.status) {
            HOST,
            MEMBER -> throw LobbyException("Cannot invite. Player already in lobby.")
            INVITED -> throw LobbyException("Cannot invite. Player already invited.")
            LEFT -> potentialPlayerInLobby.status = INVITED
        }

        lobby.addPlayer(player, INVITED)
    }

    fun removeFromInvites(playerId: Long) {
        if (!status.canInvite) {
            throw LobbyException("Cannot remove from invites. Insufficient permissions. $status")
        }

        val potentialPlayerInLobby = lobby.getPlayer(playerId)

        when (potentialPlayerInLobby.status) {
            HOST,
            MEMBER -> throw LobbyException("Cannot remove from invites. Player already in lobby.")
            LEFT -> throw LobbyException("Cannot remove from invites. Player already left.")
            INVITED -> {
                potentialPlayerInLobby.status = LEFT
                lobby.removePlayer(playerId)
            }
        }
    }

    fun leave() {
        if (!status.canLeave) {
            throw LobbyException("Cannot leave. Insufficient permissions. $status")
        }

        val previousStatus = status
        lobby.removePlayer(id)
        status = LEFT

        /* Trying to pass a HOST rights */
        if (previousStatus === HOST) {
            val members = lobby
                .findPlayers { it.value.status === MEMBER }
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
            throw LobbyException("Cannot kick. Insufficient permissions. $status")
        }

        val potentialPlayerInLobby = lobby.getPlayer(playerId)

        when (potentialPlayerInLobby.status) {
            LEFT -> throw LobbyException("Cannot kick. Player already left.")
            INVITED -> throw LobbyException("Cannot kick invited player.")
            HOST -> throw LobbyException("Cannot kick the HOST.")
            MEMBER -> {
                potentialPlayerInLobby.status = LEFT
                lobby.removePlayer(playerId)
            }
        }
    }

    companion object {
        fun from(player: PlayerDAO, status: LobbyPlayerStatus, lobby: Lobby) =
            LobbyPlayer(
                id = player.id,
                name = player.name,
                level = player.stats.level,
                status = status,
                lobby = lobby
            )
    }
}