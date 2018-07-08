package com.teamclicker.gameservice.dao

class PlayerDAO {
    var id: Long = 0
    var accountId: Long = 0

    var status: PlayerStatus = PlayerStatus.OFFLINE

    lateinit var stats: PlayerStatsDAO
    /* What palyer is currently wearing */
    lateinit var equipment: PlayerEquipmentDAO
    /* Player's backpack */
    lateinit var inventory: PlayerInventoryDAO

    var friendList: ArrayList<FriendshipDAO> = arrayListOf()
    /* Friend requests the player had received */
    var friendRequestList: ArrayList<FriendRequestDAO> = arrayListOf()
    /* Friend requests the player had sent */
    var friendRequestSentList: ArrayList<FriendRequestDAO> = arrayListOf()
}