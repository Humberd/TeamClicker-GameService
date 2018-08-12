package com.teamclicker.gameservice.controllers.helpers

import com.teamclicker.gameservice.models.dto.CreatePlayerDTO
import com.teamclicker.gameservice.models.dto.PlayerDTO
import com.teamclicker.gameservice.models.dto.UpdatePlayerDTO
import com.teamclicker.gameservice.testConfig.endpointBuilders.HttpEndpointBuilder
import com.teamclicker.gameservice.testConfig.endpointBuilders.PagedHttpEndpointBuilder
import com.teamclicker.gameservice.testConfig.endpointBuilders.TestEntity
import com.teamclicker.gameservice.utils.Generators
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpMethod.*
import org.springframework.stereotype.Service

@Service
class PlayersControllerHelper(private val http: TestRestTemplate) {
    fun create() = Create()
    fun quickCreate(withUser: TestEntity) = Create()
        .with(withUser)
        .sending(CreatePlayerDTO().also {
            it.name = Generators.randomStringId()
        })
        .expectSuccess()
        .body!!

    fun read() = Read()
    fun readAll() = ReadAll()
    fun update() = Update()
    fun delete() = Delete()

    inner class Create :
        HttpEndpointBuilder<Create, CreatePlayerDTO, PlayerDTO>(PlayerDTO::class.java, http) {
        override val url = "/api/players"
        override val method = POST
    }

    inner class Read :
        HttpEndpointBuilder<Read, Void, PlayerDTO>(PlayerDTO::class.java, http) {
        override val url = "/api/players/find"
        override val method = GET

        fun playerId(id: Long) = addQueryParam("id", id)
        fun playerName(name: String) = addQueryParam("name", name)
    }

    inner class ReadAll :
        PagedHttpEndpointBuilder<ReadAll, Void, PlayerDTO>(PlayerDTO::class.java, http) {
        override val url = "/api/players"
        override val method = GET
    }

    inner class Update :
        HttpEndpointBuilder<Update, UpdatePlayerDTO, Void>(Void::class.java, http) {
        override val url = "/api/players/{playerId}"
        override val method = PUT

        fun playerId(playerId: Long) = addPathVariable("playerId", playerId)
    }

    inner class Delete :
        HttpEndpointBuilder<Delete, Void, Void>(Void::class.java, http) {
        override val url = "/api/players/{playerId}"
        override val method = DELETE

        fun playerId(playerId: Long) = addPathVariable("playerId", playerId)

    }
}