package com.teamclicker.gameservice.models.dto

import com.teamclicker.gameservice.game.lobby.LobbyPlayerStatus

data class LobbyPlayerResponseDTO(
    val id: Long,
    val accountId: Long,
    val name: String,
    val level: Int,
    var status: LobbyPlayerStatus
)
