package com.teamclicker.gameservice.game.lobby

enum class LobbyPlayerStatus(
    var canInvite: Boolean,
    val canLeave: Boolean,
    val canKick: Boolean
) {
    HOST(true, true, true),
    MEMBER(false, true, false),
    LEFT(false, false, false),
    INVITED(false, false, false)
}