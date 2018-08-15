package com.teamclicker.gameservice.controllers.http

import com.teamclicker.gameservice.controllers.helpers.LobbyControllerHelper
import com.teamclicker.gameservice.controllers.helpers.PlayersControllerHelper
import com.teamclicker.gameservice.controllers.helpers.Users.ALICE
import com.teamclicker.gameservice.controllers.helpers.Users.BOB
import com.teamclicker.gameservice.extensions.KLogging
import com.teamclicker.gameservice.game.lobby.Lobby
import com.teamclicker.gameservice.game.lobby.LobbyStatus.PUBLIC
import com.teamclicker.gameservice.game.spring.LobbyService
import com.teamclicker.gameservice.models.dto.LobbyCreateDTO
import com.teamclicker.gameservice.models.dto.LobbyPlayerResponseDTO
import com.teamclicker.gameservice.repositories.PlayerRepository
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.util.ReflectionTestUtils
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.TimeUnit.SECONDS

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class LobbyControllerTest {

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
        fun `should join public lobby`() {
            val messageQueue = LinkedBlockingDeque<LobbyPlayerResponseDTO>()

            playersHelper.quickCreate(ALICE)
            playersHelper.quickCreate(BOB)

            val lobbyId = lobbyHelper.create()
                .with(ALICE)
                .sending(LobbyCreateDTO(status = PUBLIC))
                .expectSuccess()
                .body!!.lobbyId

            lobbyHelper.onPlayerAdded()
                .with(ALICE)
                .lobbyId(lobbyId)
                .subscribe {
                    logger.info { "Player added: $it" }
                    messageQueue.add(it)
                }

            lobbyHelper.join()
                .with(BOB)
                .lobbyId(lobbyId)
                .expectSuccess()

            assertNotNull(messageQueue.pollFirst(1, SECONDS))
        }
    }

    companion object : KLogging()
}