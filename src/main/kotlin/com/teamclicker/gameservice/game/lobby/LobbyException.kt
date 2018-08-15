package com.teamclicker.gameservice.game.lobby

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class LobbyException(message: String) : RuntimeException(message)