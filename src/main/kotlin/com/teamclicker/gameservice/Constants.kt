package com.teamclicker.gameservice


object Constants {
    const val JWT_HEADER_NAME = "Authorization"
    const val JWT_TOKEN_PREFIX = "Bearer "

    const val JWT_PUBLIC_KEY_NAME = "classpath:jwt_public_key.der"

    const val TEMPLATES_PATH = "classpath:templates"
    const val ITEM_TEMPLATES_PATH = "items.json"
    const val CREATURE_TEMPLATES_PATH = "creatures.json"
}