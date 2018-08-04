package com.teamclicker.gameservice.game.spring

import com.teamclicker.gameservice.game.core.GameException
import com.teamclicker.gameservice.game.templates.WaveTemplate
import com.teamclicker.gameservice.game.workers.WorkersSupervisor
import com.teamclicker.gameservice.models.dao.PlayerDAO
import com.teamclicker.gameservice.models.dao.PlayerStatsDAO
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class GameServiceTest {

    @Autowired
    lateinit var workersSupervisor: WorkersSupervisor
    lateinit var gameService: GameService

    @BeforeEach
    fun setUp() {
        gameService = GameService(workersSupervisor)
    }

    @Nested
    inner class CreateGame {
        @Test
        fun `should create a game`() {
            val players = listOf(PlayerDAO().also {
                it.accountId = 123
                it.stats = PlayerStatsDAO().also {
                    it.atk = 15

                }
            })
            val waves = listOf(WaveTemplate().also {
                it.templateId = 4457
                it.creatures = emptyList()
            })
            val manager = gameService.createGame(players, waves)

            assertEquals(manager, gameService.getGame(manager.gameId))
            assertEquals(1, manager.waves.size)
            assertEquals(1, manager.players.size)
        }
    }

    @Nested
    inner class GetGame {
        @Test
        fun `should throw exception when game doesn't exist`() {
            assertThrows(GameException::class.java) {
                gameService.getGame("1234")
            }
         }
    }

    @Nested
    inner class RemoveGame {
        @Test
        fun `should remove game`() {
            val manager = gameService.createGame(listOf(), listOf())

            assertEquals(manager, gameService.getGame(manager.gameId))

            gameService.removeGame(manager.gameId)

            assertThrows(GameException::class.java) {
                gameService.getGame(manager.gameId)
            }
        }

        @Test
        fun `should throw exception when removing not existing game`() {
            assertThrows(GameException::class.java) {
                gameService.removeGame("4432")
            }
        }
    }
}