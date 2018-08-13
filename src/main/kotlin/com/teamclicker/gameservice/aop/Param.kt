package com.teamclicker.gameservice.aop

import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.VALUE_PARAMETER

/**
 * Indicates that a parameter should be resolved
 * as a path variable in the message path destination.
 *
 * By default takes param name, or [value] when it's present.
 *
 * For example:
 * ```
 * @WsSendTo("/foo/bar/{name}")
 * fun sendUser(@Param name: String): String {
 *      return "Hello Alice"
 * }
 * ```
 */
@Target(VALUE_PARAMETER)
@Retention(RUNTIME)
@MustBeDocumented
annotation class Param(
    val value: String = ""
)