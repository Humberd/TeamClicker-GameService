package com.teamclicker.gameservice.controllers.helpers

import com.teamclicker.gameservice.models.dto.CreatePlayerDTO
import com.teamclicker.gameservice.models.dto.PlayerDTO
import com.teamclicker.gameservice.testConfig.endpointBuilder.EndpointBuilder
import com.teamclicker.gameservice.testConfig.endpointBuilder.PagedEndpointBuilder
import com.teamclicker.gameservice.testConfig.endpointBuilder.TestEntity
import com.teamclicker.gameservice.utils.Generators
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpMethod.GET
import org.springframework.http.HttpMethod.POST
import org.springframework.stereotype.Service

@Service
class PlayersControllerHelper(private val http: TestRestTemplate) {
    fun create() = Create()
    fun create(withUser: TestEntity) = Create()
        .with(withUser)
        .sending(CreatePlayerDTO().also {
            it.name = Generators.randomStringId()
        })
        .expectSuccess()
        .body!!

    fun read() = Read()
    fun readAll() = ReadAll()

    inner class Create :
        EndpointBuilder<Create, CreatePlayerDTO, PlayerDTO>(PlayerDTO::class.java, http) {
        override val url = "/api/players"
        override val method = POST
    }

    inner class Read :
        EndpointBuilder<Read, Void, PlayerDTO>(PlayerDTO::class.java, http) {
        override val url = "/api/players/find"
        override val method = GET

        fun playerId(id: Long) = addQueryParam("id", id)
        fun playerName(name: String) = addQueryParam("name", name)
    }

    inner class ReadAll :
        PagedEndpointBuilder<ReadAll, Void, PlayerDTO>(PlayerDTO::class.java, http) {
        override val url = "/api/players"
        override val method = GET
    }
}