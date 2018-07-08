package com.teamclicker.gameservice.dao

import java.util.*

class FriendshipDAO {
    var id: Long = 0

    lateinit var createdAt: Date

    lateinit var player1: PlayerDAO

    lateinit var player2: PlayerDAO
}
