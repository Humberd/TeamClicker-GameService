package com.teamclicker.gameservice.game.core

import com.teamclicker.gameservice.models.dao.PlayerStatsDAO

data class GamePlayer(
    val id: Long,
    val stats: PlayerStatsDAO
)