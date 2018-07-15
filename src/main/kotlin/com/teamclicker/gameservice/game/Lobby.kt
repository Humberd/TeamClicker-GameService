package com.teamclicker.gameservice.game

class Lobby(settings: LobbySettings) {
    internal val settings = settings.copy()
    internal val joinedPlayersMap = HashMap<Long, LobbyPlayer>()
    internal val invitedPlayersMap = HashMap<Long, LobbyPlayer>()

    fun invite(requesterId: Long, player: LobbyPlayer) {
        val requester = findInJoined(requesterId)

        if (!requester.status.canInvite) {
            throw LobbyException("Cannot invite. Insufficient permissions.")
        }

        if (player.status === LobbyPlayerStatus.HOST) {
            throw LobbyException("Cannot invite. Only 1 player can be host.")
        }

        invitedPlayersMap.put(player.playerId, player)
    }

    fun removeFromInvites(requesterId: Long, playerId: Long) {
        val requester = findInJoined(requesterId)

        if (!requester.status.canInvite) {
            throw LobbyException("Cannot remove from invites. Insufficient permissions.")
        }

        invitedPlayersMap.remove(playerId)
    }

    internal fun findInJoined(playerId: Long): LobbyPlayer {
        return joinedPlayersMap[playerId] ?: throw LobbyException("Cannot find player")
    }

    fun joinSelf(player: LobbyPlayer) {
        val invPlayer = invitedPlayersMap.get(player.playerId)

        if (player.status === LobbyPlayerStatus.MEMBER &&
            settings.status === LobbyStatus.PRIVATE &&
            !isInvited(invPlayer)
        ) {
            throw LobbyException("Cannot join self. Lobby is private and the Player was not invited")
        }

        invitedPlayersMap.remove(player.playerId)
        joinedPlayersMap.put(player.playerId, player)
    }

    fun leaveSelf(playerId: Long) {
        val leftPlayer = joinedPlayersMap.remove(playerId)
        if (leftPlayer === null) {
            throw LobbyException("Cannot leave self. Player not in lobby")
        }

        /* When host leaves the lobby pass it to another one */
        if (leftPlayer.status === LobbyPlayerStatus.HOST) {
            joinedPlayersMap.entries.firstOrNull()?.value?.status = LobbyPlayerStatus.HOST
        }
    }

    fun kick(requesterId: Long, playerId: Long) {
        val requester = findInJoined(requesterId)
        if (requester.status === LobbyPlayerStatus.MEMBER) {
            throw LobbyException("Cannot kick. Insufficient permissions.")
        }
        joinedPlayersMap.remove(playerId)

    }

    internal fun isInvited(player: LobbyPlayer?): Boolean {
        return player !== null
    }
}