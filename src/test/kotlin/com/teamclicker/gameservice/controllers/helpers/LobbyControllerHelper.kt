package com.teamclicker.gameservice.controllers.helpers

import com.google.gson.reflect.TypeToken
import com.teamclicker.gameservice.models.dto.*
import com.teamclicker.gameservice.testConfig.endpointBuilders.HttpEndpointBuilder
import com.teamclicker.gameservice.testConfig.endpointBuilders.WebSocketSubscriptionBuilder
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.context.annotation.Lazy
import org.springframework.http.HttpMethod.POST
import org.springframework.stereotype.Service

@Lazy
@Service
class LobbyControllerHelper(private val http: TestRestTemplate) {
    @LocalServerPort
    private val port: Int = 0

    fun create() = Create()
    fun leave() = Leave()
    fun join() = Join()
    fun invite() = Invite()
    fun uninvite() = Uninvite()
    fun kick() = Kick()

    fun onPlayerAdded() = PlayerAdded()
    fun onAllPlayers() = AllPlayers()
    fun onPlayerUpdated() = PlayerUpdated()
    fun onPlayerRemoved() = PlayerRemoved()
    fun onLobbyDisbanded() = LobbyDisbanded()

    inner class Create :
        HttpEndpointBuilder<Create, LobbyCreateDTO, LobbyCreateResponseDTO>(LobbyCreateResponseDTO::class.java, http) {
        override val url = "/api/lobbies"
        override val method = POST
    }

    inner class Leave :
        HttpEndpointBuilder<Leave, Void, Void>(Void::class.java, http) {
        override val url = "/api/lobbies/{lobbyId}/leave"
        override val method = POST

        fun lobbyId(lobbyId: String) = addPathVariable("lobbyId", lobbyId)
    }

    inner class Join :
        HttpEndpointBuilder<Join, Void, Void>(Void::class.java, http) {
        override val url = "/api/lobbies/{lobbyId}/join"
        override val method = POST

        fun lobbyId(lobbyId: String) = addPathVariable("lobbyId", lobbyId)
    }

    inner class Invite :
        HttpEndpointBuilder<Invite, LobbyInviteDTO, Void>(Void::class.java, http) {
        override val url = "/api/lobbies/{lobbyId}/invite"
        override val method = POST

        fun lobbyId(lobbyId: String) = addPathVariable("lobbyId", lobbyId)
    }

    inner class Uninvite :
        HttpEndpointBuilder<Uninvite, LobbyUninviteDTO, Void>(Void::class.java, http) {
        override val url = "/api/lobbies/{lobbyId}/uninvite"
        override val method = POST

        fun lobbyId(lobbyId: String) = addPathVariable("lobbyId", lobbyId)
    }

    inner class Kick :
        HttpEndpointBuilder<Kick, LobbyKickDTO, Void>(Void::class.java, http) {
        override val url = "/api/lobbies/{lobbyId}/kick"
        override val method = POST

        fun lobbyId(lobbyId: String) = addPathVariable("lobbyId", lobbyId)
    }

    inner class PlayerAdded :
        WebSocketSubscriptionBuilder<PlayerAdded, LobbyPlayerResponseDTO>(LobbyPlayerResponseDTO::class.java, port) {
        override val path = "/topic/lobbies/{lobbyId}/player-added"

        fun lobbyId(lobbyId: String) = addPathVariable("lobbyId", lobbyId)
    }

    inner class AllPlayers :
        WebSocketSubscriptionBuilder<AllPlayers, List<LobbyPlayerResponseDTO>>(
            TypeToken.getParameterized(
                List::class.java,
                LobbyPlayerResponseDTO::class.java
            ).type, port
        ) {
        override val path = "/topic/lobbies/{lobbyId}/add-players"

        fun lobbyId(lobbyId: String) = addPathVariable("lobbyId", lobbyId)
    }

    inner class PlayerUpdated :
        WebSocketSubscriptionBuilder<PlayerUpdated, LobbyPlayerResponseDTO>(LobbyPlayerResponseDTO::class.java, port) {
        override val path = "/topic/lobbies/{lobbyId}/player-updated"

        fun lobbyId(lobbyId: String) = addPathVariable("lobbyId", lobbyId)
    }

    inner class PlayerRemoved :
        WebSocketSubscriptionBuilder<PlayerRemoved, Long>(Long::class.java, port) {
        override val path = "/topic/lobbies/{lobbyId}/player-removed"

        fun lobbyId(lobbyId: String) = addPathVariable("lobbyId", lobbyId)
    }

    inner class LobbyDisbanded :
        WebSocketSubscriptionBuilder<LobbyDisbanded, Void>(Void::class.java, port) {
        override val path = "/topic/lobbies/{lobbyId}/disbanded"

        fun lobbyId(lobbyId: String) = addPathVariable("lobbyId", lobbyId)
    }
}