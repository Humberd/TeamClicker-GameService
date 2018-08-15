package com.teamclicker.gameservice.game.lobby

import com.teamclicker.gameservice.game.lobby.LobbyPlayerStatus.*
import com.teamclicker.gameservice.models.dao.PlayerDAO

data class LobbyPlayer(
    val id: Long,
    val accountId: Long,
    val name: String,
    val level: Int,
    var status: LobbyPlayerStatus,
    internal val lobby: Lobby
) {
    fun invite(player: PlayerDAO) {
        if (!status.canInvite) {
            throw LobbyException("Cannot invite. Insufficient permissions. $status")
        }

        if (lobby.hasPlayer(player.id)) {
            throw LobbyException("Player already invited or in the lobby.")
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
            INVITED -> {
                lobby.removePlayer(playerId)
            }
        }
    }

    fun leave() {
        if (!status.canLeave) {
            throw LobbyException("Cannot leave. Insufficient permissions. $status")
        }

        lobby.removePlayer(id)

        /* Trying to pass HOST rights */
        if (status === HOST) {
            val members = lobby
                .findPlayers { it.value.status === MEMBER }
                .map { it.value }

            if (members.isEmpty()) {
                lobby.disband()
                return
            }

            val member = members.first().also {
                it.status = HOST
            }
            lobby.updatePlayer(member)
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
            INVITED -> throw LobbyException("Cannot kick invited player.")
            HOST -> throw LobbyException("Cannot kick the HOST.")
            MEMBER -> {
                lobby.removePlayer(playerId)
            }
        }
    }

    companion object {
        fun from(player: PlayerDAO, status: LobbyPlayerStatus, lobby: Lobby) =
            LobbyPlayer(
                id = player.id,
                accountId = player.accountId,
                name = player.name,
                level = player.stats.level,
                status = status,
                lobby = lobby
            )
    }
}