package com.teamclicker.gameservice.controllers.helpers

import com.teamclicker.gameservice.models.dto.CreatePlayerDTO
import com.teamclicker.gameservice.models.dto.PlayerDTO
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpMethod.GET
import org.springframework.http.HttpMethod.POST
import org.springframework.stereotype.Service

@Service
class PlayersControllerHelper(private val http: TestRestTemplate) {
    fun create() = Create()
    fun read() = Read()

    inner class Create :
        EndpointBuilder<Create, CreatePlayerDTO, PlayerDTO>(PlayerDTO::class.java, http) {
        override val url = "/api/players"
        override val method = POST
    }

    inner class Read :
        EndpointBuilder<Read, Void, PlayerDTO>(PlayerDTO::class.java, http) {
        override val url = "/api/players/{playerId}"
        override val method = GET

        fun playerId(playerId: Long) = addPathVariable("playerId", playerId)
    }
}