package com.teamclicker.gameservice.game

data class LobbyPlayer(
    val playerId: Long,
    var status: LobbyPlayerStatus,
    val name: String,
    val level: Int
) {

}