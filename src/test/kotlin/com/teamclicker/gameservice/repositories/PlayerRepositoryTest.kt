package com.teamclicker.gameservice.repositories

import com.teamclicker.gameservice.dao.*
import mu.KLogging
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class PlayerRepositoryTest {

    @Autowired
    lateinit var playerRepository: PlayerRepository

    @Test
    fun `should foo`() {
        val player = PlayerDAO().also {
            it.stats = PlayerStatsDAO()
            it.equipment = PlayerEquipmentDAO.create()
            it.inventory = PlayerInventoryDAO().also {
                it.itemList = listOf(
                    InventoryItemSlotDAO().also {
                        it.count = 2
                    },
                    InventoryItemSlotDAO()
                )
            }
        }
        playerRepository.save(player)

        val player2 = PlayerDAO().also {pl ->
            pl.stats = PlayerStatsDAO()
            pl.equipment = PlayerEquipmentDAO.create()
            pl.inventory = PlayerInventoryDAO()
            pl.friendList = listOf(FriendshipDAO().also {
                it.owner = pl
                it.friend = player
            })
        }

        playerRepository.save(player2)

    }

    @Test
    fun `should foo1`() {
        val players = playerRepository.findAll()
        logger.info { players }
    }

    @Test
    fun `should `() {
        playerRepository.deleteAll()
    }

    companion object: KLogging()
}