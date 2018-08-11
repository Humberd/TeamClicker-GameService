package com.teamclicker.gameservice.game.templates

class TemplateNotExistException(message: String) : RuntimeException(message)

class TemplateParsingException(message: String) : RuntimeException(message)