package com.teamclicker.gameservice.repositories

import mu.KLogging
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class PlayerRepositoryTest {

    @Autowired
    lateinit var playerRepository: PlayerRepository

//    @Test
//    fun `should foo`() {
//        val player = PlayerDAO().also {
//            it.stats = PlayerStatsDAO()
//            it.name = "Xyz"
//            it.equipment = PlayerEquipmentDAO.create()
//            it.inventory = PlayerInventoryDAO().also {
//                it.itemList = listOf(
//                    InventoryItemSlotDAO().also {
//                        it.count = 2
//                    },
//                    InventoryItemSlotDAO()
//                )
//            }
//        }
//        playerRepository.save(player)
//
//        val player2 = PlayerDAO().also { pl ->
//            pl.stats = PlayerStatsDAO()
//            pl.name = "abc"
//            pl.equipment = PlayerEquipmentDAO.create()
//            pl.inventory = PlayerInventoryDAO()
//            pl.friendList = listOf(FriendshipDAO().also {
//                it.owner = pl
//                it.friend = player
//            })
//            pl.friendRequestList = listOf(
//                FriendRequestDAO().also {
//                    it.sender = pl
//                    it.receiver = player
//                }
//            )
//        }
//
//        playerRepository.save(player2)
//
//    }
//
//    @Test
//    @Transactional
//    fun `should foo1`() {
//        val players = playerRepository.findAll()
//        players.map { it.friendList }
//            .forEach { logger.info { it }}
//    }

    companion object : KLogging()
}