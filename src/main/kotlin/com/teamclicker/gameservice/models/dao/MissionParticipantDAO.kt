package com.teamclicker.gameservice.models.dao

import com.teamclicker.gameservice.game.Lobby
import com.teamclicker.gameservice.game.LobbySettings
import com.teamclicker.gameservice.game.LobbyStatus

class MissionParticipantDAO {
    var id: Long = 0

    lateinit var player: PlayerDAO

    lateinit var mission: MissionDAO
}