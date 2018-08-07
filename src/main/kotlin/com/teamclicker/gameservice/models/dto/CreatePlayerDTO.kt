package com.teamclicker.gameservice.models.dto

import javax.validation.constraints.NotBlank

class CreatePlayerDTO {
    @NotBlank
    lateinit var name: String
}