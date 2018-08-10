package com.teamclicker.gameservice.controllers.http

import com.teamclicker.gameservice.controllers.helpers.PlayersControllerHelper
import com.teamclicker.gameservice.controllers.helpers.Users.ANONYMOUS
import com.teamclicker.gameservice.models.dto.CreatePlayerDTO
import com.teamclicker.gameservice.repositories.PlayerRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class PlayersControllerTest {
    @Autowired
    lateinit var playerRepository: PlayerRepository
    @Autowired
    lateinit var playersHelper: PlayersControllerHelper

    @Nested
    inner class Create {
        @BeforeEach
        fun setUp() {
            playerRepository.deleteAll()
        }

        @Test
        fun `should not create when user is UNAUTHORIZED`() {
            val body = CreatePlayerDTO().also {
                it.name = "Foobabr"
            }
            playersHelper.create()
                .with(ANONYMOUS)
                .sending(body)
                .expectError {
                    assertEquals(403, it.statusCodeValue)
                }
        }
    }
}