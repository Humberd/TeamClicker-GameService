package com.teamclicker.gameservice.dto

import com.teamclicker.gameservice.Constants
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

class EPChangePasswordDTO {
    @NotBlank
    var oldPassword: String? = null

    @NotBlank
    @Size(min = Constants.MIN_PASSWORD_SIZE)
    var newPassword: String? = null
}