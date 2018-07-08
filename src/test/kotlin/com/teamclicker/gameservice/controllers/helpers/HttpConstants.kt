package com.teamclicker.gameservice.controllers.helpers

import com.teamclicker.gameservice.testConfig.models.UserAccountMock

object HttpConstants {
    val ALICE
        get() = UserAccountMock(
            email = "alice@alice.com",
            password = "alicePassword"
        )
    val BOB
        get() = UserAccountMock(
            email = "bobe@bob.com",
            password = "bobPassword"
        )
    val CHUCK
        get() = UserAccountMock(
            email = "chuck@chuck.com",
            password = "chuckPassword"
        )
    val DAVE_ADMIN
        get() = UserAccountMock(
            email = "dave@admin.com",
            password = "adminPassword"
        )
    val ANONYMOUS: UserAccountMock?
        get() = null
}