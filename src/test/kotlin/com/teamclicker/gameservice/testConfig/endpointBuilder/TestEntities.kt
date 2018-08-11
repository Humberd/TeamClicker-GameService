package com.teamclicker.gameservice.testConfig.endpointBuilder

sealed class TestEntity(
    val token: String?
)

class UserMock(
    token: String
) : TestEntity(token)

class AdminMock(
    token: String
) : TestEntity(token)

class Anonymous : TestEntity(null)