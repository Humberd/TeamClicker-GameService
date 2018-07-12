package com.teamclicker.gameservice.dao

class MissionParticipantDAO {
    var id: Long = 0

    lateinit var player: PlayerDAO

    lateinit var mission: MissionDAO
}