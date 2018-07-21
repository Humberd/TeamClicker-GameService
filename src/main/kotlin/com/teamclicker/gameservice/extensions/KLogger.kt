package com.teamclicker.gameservice.extensions

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import mu.KLoggable
import mu.KLogger

open class KLogging(
    level: Level? = null
) : KLoggable {
    override val logger: KLogger = this.logger().also {
        if (level != null) {
            (it.underlyingLogger as Logger).level = level
        }
    }
}