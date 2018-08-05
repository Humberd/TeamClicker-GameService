package com.teamclicker.gameservice.game.templates

data class MissionTemplate(
    val templateId: Long,
    var waves: List<WaveTemplate>
)
