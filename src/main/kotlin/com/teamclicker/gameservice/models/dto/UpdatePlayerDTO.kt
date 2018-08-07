package com.teamclicker.gameservice.models.dto

import javax.validation.constraints.NotBlank

class UpdatePlayerDTO {
    @NotBlank
    lateinit var name: String
}