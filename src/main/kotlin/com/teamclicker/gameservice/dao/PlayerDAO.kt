package com.teamclicker.gameservice.dao

class PlayerDAO {
    var id: Long = 0
    var accountId: Long = 0

    lateinit var stats: PlayerStatsDAO
    /* What palyer is currently wearing */
    lateinit var equipment: PlayerEquipmentDAO

    lateinit var inventory: PlayerInventoryDAO

    var friendsList: ArrayList<FriendshipDAO> = arrayListOf()
    var friendRequestList: ArrayList<FriendRequestDAO> = arrayListOf()
}