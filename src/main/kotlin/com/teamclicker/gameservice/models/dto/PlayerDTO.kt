package com.teamclicker.gameservice.models.dto

import com.teamclicker.gameservice.models.dao.PlayerStatus
import java.time.LocalDateTime

data class PlayerDTO(
    val id: Long,
    val accountId: Long,
    val name: String,
    val createdAt: LocalDateTime,
    val status: PlayerStatus
)