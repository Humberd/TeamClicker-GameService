package com.teamclicker.gameservice.dao

import java.util.*

class FriendRequestDAO {
    var id: Long = 0

    lateinit var createdAt: Date

    var completedAt: Date? = null

    lateinit var sender: PlayerDAO

    lateinit var receiver: PlayerDAO

    fun isCompleted() = completedAt !== null
}
