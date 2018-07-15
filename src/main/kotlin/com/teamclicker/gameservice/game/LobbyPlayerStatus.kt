package com.teamclicker.gameservice.game

enum class LobbyPlayerStatus(
    var canInvite: Boolean
) {
    HOST(true),
    MEMBER(false);
}