package com.teamclicker.gameservice.aop

/**
 * Defines a path that a method return value should be sent to:
 *
 * For example:
 *
 * ```
 * @WsSendTo("/foo/bar")
 * fun sendUser(): String {
 *      return "Hello Alice"
 * }
 * ```
 *
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class WsSendTo(
    val path: String
)