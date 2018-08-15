package com.teamclicker.gameservice.models.dto

import com.teamclicker.gameservice.game.lobby.LobbyStatus

data class LobbyCreateDTO(
    var status: LobbyStatus
)