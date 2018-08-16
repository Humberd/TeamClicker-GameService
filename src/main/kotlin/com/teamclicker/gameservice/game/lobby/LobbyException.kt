package com.teamclicker.gameservice.game.lobby

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseBody
@ResponseStatus(HttpStatus.LOCKED) // 423
class LobbyException(message: String) : RuntimeException(message)