package com.teamclicker.gameservice.dto

import javax.validation.constraints.NotBlank

class EPSignInDTO {
    @NotBlank
    var email: String? = null

    @NotBlank
    var password: String? = null
}