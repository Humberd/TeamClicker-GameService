package com.teamclicker.gameservice.mappers

abstract class AbstractMapper<FROM, TO> {
    abstract fun parse(from: FROM): TO
}