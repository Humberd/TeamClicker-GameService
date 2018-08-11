package com.teamclicker.gameservice.controllers.http

import com.teamclicker.gameservice.controllers.helpers.PlayersControllerHelper
import com.teamclicker.gameservice.controllers.helpers.Users.ALICE
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
        fun `should create a player`() {
            val body = CreatePlayerDTO().also {
                it.name = "Mark"
            }
            playersHelper.create()
                .with(ALICE)
                .sending(body)
                .expectSuccess()
        }

        @Test
        fun `should not create a player when user is UNAUTHORIZED`() {
            val body = CreatePlayerDTO().also {
                it.name = "Foobar"
            }
            playersHelper.create()
                .with(ANONYMOUS)
                .sending(body)
                .expectError(403)
        }

        @Test
        fun `should not create a player when player with that name already exists`() {
            val body = CreatePlayerDTO().also {
                it.name = "Foobar"
            }
            playersHelper.create()
                .with(ALICE)
                .sending(body)
                .expectSuccess()

            val body2 = CreatePlayerDTO().also {
                it.name = "foobar"
            }
            playersHelper.create()
                .with(ALICE)
                .sending(body2)
                .expectError(410)
        }
    }
    
    @Nested
    inner class Read {
        @BeforeEach
        fun setUp() {
            playerRepository.deleteAll()
        }
        @Test
        fun `should not read player when player does not exist`() {

        }
    }
}