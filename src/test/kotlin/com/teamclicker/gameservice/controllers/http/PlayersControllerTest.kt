package com.teamclicker.gameservice.controllers.http

import com.teamclicker.gameservice.controllers.helpers.PlayersControllerHelper
import com.teamclicker.gameservice.controllers.helpers.Users.ALICE
import com.teamclicker.gameservice.controllers.helpers.Users.ANONYMOUS
import com.teamclicker.gameservice.controllers.helpers.Users.BOB
import com.teamclicker.gameservice.models.dto.CreatePlayerDTO
import com.teamclicker.gameservice.models.dto.UpdatePlayerDTO
import com.teamclicker.gameservice.repositories.PlayerRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
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
        fun `should not read player when user is UNAUTHORIZED`() {
            playersHelper.read()
                .with(ANONYMOUS)
                .playerId(1234)
                .expectError(403)
        }

        @Test
        fun `should not read player when player does not exist`() {
            playersHelper.read()
                .with(ALICE)
                .playerId(1234)
                .expectError(404)
        }

        @Test
        fun `should read player by id`() {
            val player = playersHelper.quickCreate(ALICE)

            playersHelper.read()
                .with(ALICE)
                .playerId(player.id)
                .expectSuccess {
                    assertEquals(player, it.body)
                }
        }

        @Test
        fun `should read player by name`() {
            val player = playersHelper.quickCreate(ALICE)

            playersHelper.read()
                .with(ALICE)
                .playerName(player.name.toUpperCase())
                .expectSuccess {
                    assertEquals(player, it.body)
                }
        }

        @Test
        fun `should read player by id when 2 params were presented`() {
            val player = playersHelper.quickCreate(ALICE)

            playersHelper.read()
                .with(ALICE)
                .playerId(player.id)
                .playerName("not existing name")
                .expectSuccess {
                    assertEquals(player, it.body)
                }
        }

    }

    @Nested
    inner class ReadAll {
        @BeforeEach
        fun setUp() {
            playerRepository.deleteAll()
        }

        @Test
        fun `should read all users`() {
            playersHelper.quickCreate(ALICE)
            playersHelper.quickCreate(ALICE)
            playersHelper.quickCreate(ALICE)

            playersHelper.readAll()
                .with(ALICE)
                .expectSuccess {
                    assertEquals(3, it.body?.content?.size)
                }
        }
    }

    @Nested
    inner class Update {
        @BeforeEach
        fun setUp() {
            playerRepository.deleteAll()
        }

        @Test
        fun `should not update when providing invalid playerId`() {
            val body = UpdatePlayerDTO().also {
                it.name = "Foobar"
            }
            playersHelper.update()
                .with(ALICE)
                .sending(body)
                .playerId(123456)
                .expectError(404)
        }

        @Test
        fun `should not update a player when it doesn't belong to the account`() {
            val alicePlayer1 = playersHelper.quickCreate(ALICE)

            val body = UpdatePlayerDTO().also {
                it.name = "Foobar444"
            }
            playersHelper.update()
                .with(BOB)
                .sending(body)
                .playerId(alicePlayer1.id)
                .expectError(404)

            playersHelper.read()
                .with(BOB)
                .playerName("Foobar444")
                .expectError(404)
        }

        @Test
        fun `should update a player only when it belongs to the account`() {
            val alicePlayer1 = playersHelper.quickCreate(ALICE)

            val body = UpdatePlayerDTO().also {
                it.name = "Foobar444"
            }
            playersHelper.update()
                .with(ALICE)
                .sending(body)
                .playerId(alicePlayer1.id)
                .expectSuccess()

            playersHelper.read()
                .with(ALICE)
                .playerName("Foobar444")
                .expectSuccess {
                    assertEquals(alicePlayer1.copy(name = body.name), it.body)
                }
        }
    }

    @Nested
    inner class Delete {
        @BeforeEach
        fun setUp() {
            playerRepository.deleteAll()
        }

        @Test
        fun `should not delete a non existing player`() {
            playersHelper.delete()
                .with(ALICE)
                .playerId(9890)
                .expectError(404)
        }

        @Test
        fun `should not delete a player when it doesn't belong to the account`() {
            val alicePlayer1 = playersHelper.quickCreate(ALICE)

            playersHelper.delete()
                .with(BOB)
                .playerId(alicePlayer1.id)
                .expectError(404)

            playersHelper.read()
                .with(BOB)
                .playerId(alicePlayer1.id)
                .expectSuccess()
        }

        @Test
        fun `should update a player only when it delongs to the account`() {
            val alicePlayer1 = playersHelper.quickCreate(ALICE)

            playersHelper.delete()
                .with(ALICE)
                .playerId(alicePlayer1.id)
                .expectSuccess()

            playersHelper.read()
                .with(BOB)
                .playerId(alicePlayer1.id)
                .expectError(404)
        }
    }
}