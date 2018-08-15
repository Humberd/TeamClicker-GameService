package com.teamclicker.gameservice.aop

import ch.qos.logback.classic.Level.TRACE
import com.teamclicker.gameservice.extensions.KLogging
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Component
import java.util.*
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.kotlinFunction

@Aspect
@Component
class WebSocketAspect(
    private val simpleMessagingTemplate: SimpMessagingTemplate
) {
    /**
     * Cannot send a message without a body,
     * so in this case it sends an empty object.
     */
    private final val emptyObject = object : Any() {
        override fun toString() = "EmptyObject"
    }

    @Around("execution(@com.teamclicker.gameservice.aop.WsSendTo * *(..))")
    fun handleMessage(point: ProceedingJoinPoint): Any? {
        val returnValue = point.proceed()

        val methodSignature = point.signature as MethodSignature
        val rawDestinationPath = getDestination(methodSignature)
        val params = getDestinationPathParams(methodSignature, point.args)

        val destinationPath = resolveVariables(rawDestinationPath, params)

        val parameterPayload = getPayloadFromMethodParameter(methodSignature, point.args)
        val payload = if (hasResponsePayload(methodSignature)) {
            returnValue
        } else if (parameterPayload.isPresent) {
            parameterPayload.get()
        } else {
            emptyObject
        }

        val userId = getUserId(methodSignature, point.args)
        if (!userId.isPresent) {
            logger.trace { "Path: $destinationPath. Payload: $payload" }
            simpleMessagingTemplate.convertAndSend(destinationPath, payload)
        } else {
            logger.trace { "Path: $destinationPath. Payload: $payload. User: ${userId.get()}" }
            simpleMessagingTemplate.convertAndSendToUser(userId.get().toString(), destinationPath, payload)
        }

        return returnValue
    }

    fun <T> getUserId(methodSignature: MethodSignature, args: Array<T>): Optional<T> {
        val valueParameters = methodSignature.method.kotlinFunction!!.valueParameters

        for (i in 0..valueParameters.size - 1) {
            if (valueParameters[i].findAnnotation<User>() === null) {
                continue
            }

            return Optional.of(args[i])
        }

        return Optional.empty()
    }

    fun <T> getPayloadFromMethodParameter(methodSignature: MethodSignature, args: Array<T>): Optional<T> {
        val valueParameters = methodSignature.method.kotlinFunction!!.valueParameters

        for (i in 0..valueParameters.size - 1) {
            if (valueParameters[i].findAnnotation<Payload>() === null) {
                continue
            }

            return Optional.of(args[i])
        }

        return Optional.empty()
    }

    fun hasResponsePayload(methodSignature: MethodSignature): Boolean {
        return methodSignature.method.getAnnotation(Payload::class.java) !== null
    }

    fun getDestination(methodSignature: MethodSignature): String {
        return methodSignature.method.getAnnotation(WsSendTo::class.java).value
    }

    /**
     * Returns map of variable names to its values
     */
    fun getDestinationPathParams(methodSignature: MethodSignature, args: Array<Any>): Map<String, Any> {
        val valueParameters = methodSignature.method.kotlinFunction!!.valueParameters

        return combineLists(valueParameters, args.toList())
            .mapKeys { (parameter, _) ->
                val annotation = parameter.findAnnotation<Param>()
                if (annotation === null) {
                    return@mapKeys null
                }
                return@mapKeys if (annotation.value.isBlank()) {
                    parameter.name!!
                } else {
                    annotation.value
                }
            }
            .filterKeys { it !== null }
            .mapKeys { it.key as String }
    }

    fun <Key, Value> combineLists(keyList: Iterable<Key>, valueList: Iterable<Value>): Map<Key, Value> {
        return keyList.zip(valueList)
            .toMap()
    }

    /**
     * Replaces [path] variables surrounded with `{}` with values in [variables]
     */
    fun resolveVariables(path: String, variables: Map<String, Any>): String {
        var newPath = path
        for ((key, value) in variables) {
            newPath = newPath.replace("{$key}", value.toString())
        }
        return newPath
    }

    companion object : KLogging(TRACE)
}