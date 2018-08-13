package com.teamclicker.gameservice.aop

import kotlin.annotation.AnnotationRetention.*
import kotlin.annotation.AnnotationTarget.*

/**
 * Defines a path that a method return value should be sent to:
 *
 * For example:
 * ```
 * @WsSendTo("/foo/bar")
 * @Payload
 * fun sendUser(): String {
 *      return "Hello Alice"
 * }
 * ```
 *
 */
@Target(FUNCTION)
@Retention(RUNTIME)
@MustBeDocumented
annotation class WsSendTo(
    val value: String
)
