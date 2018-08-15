package com.teamclicker.gameservice.game.spring

import com.teamclicker.gameservice.aop.Param
import com.teamclicker.gameservice.aop.Payload
import com.teamclicker.gameservice.aop.WsSendTo
import com.teamclicker.gameservice.game.lobby.LobbyPlayer
import org.springframework.stereotype.Service

@Service
class WsLobbyAPI {

    @WsSendTo("/topic/lobbies/{lobbyId}/player-removed")
    fun sendPlayerRemoved(@Param lobbyId: String, @Payload playerId: Long) {
    }

    @WsSendTo("/topic/lobbies/{lobbyId}/player-added")
    fun sendPlayerAdded(@Param lobbyId: String, @Payload lobbyPlayer: LobbyPlayer) {
    }

    @WsSendTo("/topic/lobbies/{lobbyId}/player-updated")
    fun sendPlayerUpdated(@Param lobbyId: String, @Payload lobbyPlayer: LobbyPlayer) {
    }

    @WsSendTo("/topic/lobbies/{lobbyId}/all-players")
    fun sendAllPlayers(@Param lobbyId: String, @Payload lobbyPlayers: List<LobbyPlayer>) {
    }

    @WsSendTo("/topic/lobbies/{lobbyId}/disbanded")
    fun sendLobbyDisband(@Param lobbyId: String) {
    }

    @WsSendTo("/topic/lobbies/{lobbyId}/status-changed")
    fun sendStatusChanged(@Param lobbyId: String) {
    }
}