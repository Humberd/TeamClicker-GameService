package com.teamclicker.gameservice.defaults

import com.teamclicker.gameservice.models.dao.*
import org.springframework.stereotype.Service

@Service
class PlayerDefaults {
    fun newPlayer(accountId: Long, name: String): PlayerDAO {
        return PlayerDAO().also { pl ->
            pl.accountId = accountId
            pl.name = name
            pl.status = PlayerStatus.ONLINE
            pl.stats = PlayerStatsDAO().also {
                it.level = 1
                it.experience = 0
                it.maxHp = 100
                it.hp = 100
                it.atk = 10
                it.def = 10
                it.gold = 0
                it.diamonds = 0
            }
            pl.equipment = PlayerEquipmentDAO.create()
            pl.inventory = PlayerInventoryDAO().also {
                it.itemList = arrayListOf()
            }
            pl.friendList = arrayListOf()
            pl.friendRequestList = arrayListOf()
            pl.friendRequestSentList = arrayListOf()
        }
    }
}