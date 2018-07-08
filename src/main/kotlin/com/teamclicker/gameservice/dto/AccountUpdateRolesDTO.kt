package com.teamclicker.gameservice.dto

import com.teamclicker.gameservice.validators.ValidateRoles
import javax.validation.constraints.NotNull

class AccountUpdateRolesDTO {
    @NotNull
    @field:ValidateRoles
    var roles: Set<String> = emptySet()
}