package com.teamclicker.gameservice.controllers.http

import com.teamclicker.gameservice.controllers.helpers.LobbyControllerHelper
import com.teamclicker.gameservice.controllers.helpers.PlayersControllerHelper
import com.teamclicker.gameservice.controllers.helpers.Users.ALICE
import com.teamclicker.gameservice.controllers.helpers.Users.BOB
import com.teamclicker.gameservice.extensions.KLogging
import com.teamclicker.gameservice.game.lobby.Lobby
import com.teamclicker.gameservice.game.lobby.LobbyStatus.PRIVATE
import com.teamclicker.gameservice.game.lobby.LobbyStatus.PUBLIC
import com.teamclicker.gameservice.game.spring.LobbyService
import com.teamclicker.gameservice.models.dto.LobbyCreateDTO
import com.teamclicker.gameservice.models.dto.LobbyInviteDTO
import com.teamclicker.gameservice.repositories.PlayerRepository
import com.teamclicker.gameservice.testConfig.endpointBuilders.Unsubscribeable
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.util.ReflectionTestUtils

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class LobbyControllerTest {
    val subscriptions = ArrayList<Unsubscribeable>()

    @Autowired
    lateinit var playerRepository: PlayerRepository
    @Autowired
    lateinit var playersHelper: PlayersControllerHelper
    @Autowired
    lateinit var lobbyHelper: LobbyControllerHelper

    @Autowired
    lateinit var lobbyService: LobbyService

    var lobbyMap = HashMap<String, Lobby>()
    @BeforeEach
    fun setUp() {
        playerRepository.deleteAll()
        lobbyMap.clear()
        ReflectionTestUtils.setField(lobbyService, "lobbyMap", lobbyMap)
    }

    @AfterEach
    fun cleanUp() {
        for (subscription in subscriptions) {
            subscription.unsubscribe()
        }
        subscriptions.clear()
    }

    @Nested
    inner class Create {
        @Test
        fun `should create a lobby`() {
            playersHelper.quickCreate(ALICE)

            val body = LobbyCreateDTO(
                status = PUBLIC
            )
            lobbyHelper.create()
                .with(ALICE)
                .sending(body)
                .expectSuccess {
                    assert(it.body!!.lobbyId.isNotBlank())
                }
        }
    }

    @Nested
    inner class Join {
        @Test
        fun `should join PUBLIC lobby`() {
            playersHelper.quickCreate(ALICE)
            playersHelper.quickCreate(BOB)

            /* ALICE creates a lobby */
            val lobbyId = lobbyHelper.create()
                .with(ALICE)
                .sending(LobbyCreateDTO(status = PUBLIC))
                .expectSuccess()
                .body!!.lobbyId

            /* ALICE subscribes to new players */
            val alicePlayerAddedSub =
                lobbyHelper.onPlayerAdded()
                    .with(ALICE)
                    .lobbyId(lobbyId)
                    .subscribe().also {
                        subscriptions.add(it)
                    }

            /* BOB joins ALICE'S lobby */
            lobbyHelper.join()
                .with(BOB)
                .lobbyId(lobbyId)
                .expectSuccess()

            alicePlayerAddedSub.expectEvent()
        }

        @Test
        fun `should not join PRIVATE lobby without invitation`() {
            playersHelper.quickCreate(ALICE)
            playersHelper.quickCreate(BOB)

            /* ALICE creates a lobby */
            val lobbyId = lobbyHelper.create()
                .with(ALICE)
                .sending(LobbyCreateDTO(status = PRIVATE))
                .expectSuccess()
                .body!!.lobbyId

            /* ALICE subscribes to new players */
            val alicePlayerAddedSub =
                lobbyHelper.onPlayerAdded()
                    .with(ALICE)
                    .lobbyId(lobbyId)
                    .subscribe().also {
                        subscriptions.add(it)
                    }

            /* BOB joins ALICE'S lobby */
            lobbyHelper.join()
                .with(BOB)
                .lobbyId(lobbyId)
                .expectError(423)

            alicePlayerAddedSub.expectNoEvent()
        }

        @Test
        fun `should join PRIVATE lobby with invitation`() {
            playersHelper.quickCreate(ALICE)
            val bobPlayer = playersHelper.quickCreate(BOB)

            /* ALICE creates a lobby */
            val lobbyId = lobbyHelper.create()
                .with(ALICE)
                .sending(LobbyCreateDTO(status = PRIVATE))
                .expectSuccess()
                .body!!.lobbyId

            /* ALICE subscribes to new players */
            val alicePlayerAddedSub =
                lobbyHelper.onPlayerAdded()
                    .with(ALICE)
                    .lobbyId(lobbyId)
                    .subscribe().also {
                        subscriptions.add(it)
                    }

            /* ALICE invites BOB */
            lobbyHelper.invite()
                .with(ALICE)
                .lobbyId(lobbyId)
                .sending(LobbyInviteDTO(bobPlayer.id))
                .expectSuccess()

            /* BOB joins ALICE'S lobby */
            lobbyHelper.join()
                .with(BOB)
                .lobbyId(lobbyId)
                .expectSuccess()

            alicePlayerAddedSub.expectEvent()
        }

        // TODO: player can't be in more than 1 lobby
    }

    companion object : KLogging()
}