package com.teamclicker.gameservice.controllers.helpers

import com.teamclicker.gameservice.models.dto.CreatePlayerDTO
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpMethod.POST
import org.springframework.stereotype.Service

@Service
class PlayersControllerHelper(private val http: TestRestTemplate) {
    fun create() = Create()

    inner class Create :
        EndpointBuilder<Create, CreatePlayerDTO, Void>(Void::class.java, http) {
        override val url = "/api/players"
        override val method = POST
    }
}