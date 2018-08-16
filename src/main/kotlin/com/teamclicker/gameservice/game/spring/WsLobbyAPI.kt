package com.teamclicker.gameservice.game.spring

import com.teamclicker.gameservice.aop.Param
import com.teamclicker.gameservice.aop.Payload
import com.teamclicker.gameservice.aop.WsSendTo
import com.teamclicker.gameservice.models.dto.LobbyPlayerResponseDTO
import org.springframework.stereotype.Service

@Service
class WsLobbyAPI {

    @WsSendTo("/topic/lobbies/{lobbyId}/player-added")
    fun sendPlayerAdded(@Param lobbyId: String, @Payload lobbyPlayer: LobbyPlayerResponseDTO) {
    }

    @WsSendTo("/user/lobbies/{lobbyId}/all-players")
    fun sendAllPlayers(@Param lobbyId: String, @Payload lobbyPlayers: List<LobbyPlayerResponseDTO>) {
    }

    @WsSendTo("/topic/lobbies/{lobbyId}/player-updated")
    fun sendPlayerUpdated(@Param lobbyId: String, @Payload lobbyPlayer: LobbyPlayerResponseDTO) {
    }

    @WsSendTo("/topic/lobbies/{lobbyId}/player-removed")
    fun sendPlayerRemoved(@Param lobbyId: String, @Payload playerId: Long) {
    }

    @WsSendTo("/topic/lobbies/{lobbyId}/disbanded")
    fun sendLobbyDisbanded(@Param lobbyId: String) {
    }
}