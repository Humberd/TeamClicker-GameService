package com.teamclicker.gameservice.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseBody
@ResponseStatus(HttpStatus.BAD_REQUEST) // 400
class InvalidRequestBodyException(message: String) : RuntimeException(message)

@ResponseBody
@ResponseStatus(HttpStatus.BAD_REQUEST) // 400
class InvalidParametersException(message: String) : RuntimeException(message)

@ResponseBody()
@ResponseStatus(HttpStatus.UNAUTHORIZED) // 401
class InvalidCredentialsException(message: String) : RuntimeException(message)

@ResponseBody
@ResponseStatus(HttpStatus.NOT_FOUND) // 404
class EntityDoesNotExistException(message: String) : RuntimeException(message)

@ResponseBody
@ResponseStatus(HttpStatus.GONE) // 410
class EntityAlreadyExistsException(message: String) : RuntimeException(message)

@ResponseBody
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 500
class InternalServerErrorException(message: String) : RuntimeException(message)
