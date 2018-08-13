package com.teamclicker.gameservice.aop

import kotlin.annotation.AnnotationRetention.*
import kotlin.annotation.AnnotationTarget.*

/**
 * Indicates that given paremeter should be sent as a message payload
 *
 * This example shows that after method method returns it will send a message to
 * "/foo/bar" with payload of [body]
 * ```
 * @WsSendTo("/foo/bar")
 * fun sendUser(@Payload body: String) {
 *      println("hello")
 * }
 * ```
 *
 * When the method is annotated with [Payload] then its return value
 * will be sent as payload.
 *
 * When both the method and the param are annotated with [Payload],
 * then method return value takes precedence.
 *
 * When more than one parameter is annotated with [Payload],
 * then exception will be raised.
 */
@Target(VALUE_PARAMETER, FUNCTION)
@Retention(RUNTIME)
@MustBeDocumented
annotation class Payload
