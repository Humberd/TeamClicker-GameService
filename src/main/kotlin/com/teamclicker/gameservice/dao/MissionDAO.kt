package com.teamclicker.gameservice.dao

class MissionDAO {
    var id: Long = 0

    lateinit var mission: MissionTemplate

    var participants: ArrayList<MissionParticipantDAO> = arrayListOf()

    var requiredLevel: Int = 0

    var maxPlayers: Int = 0
}