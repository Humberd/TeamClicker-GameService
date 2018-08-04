package com.teamclicker.gameservice.aop

import com.teamclicker.gameservice.extensions.KLogging
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.intellij.lang.annotations.Language
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Component
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

@Aspect
@Component
class WebSocketAspect(
    val simpleMessagingTemplate: SimpMessagingTemplate
) {

    @Around("execution(@com.teamclicker.gameservice.aop.WsSendTo * *(..))")
    fun handleException(point: ProceedingJoinPoint): Any {
        val returnValue = point.proceed()

        val methodSignature = point.signature as MethodSignature
        val annotation = methodSignature.method.getAnnotation(WsSendTo::class.java)
        val endpoint = annotation.path

        simpleMessagingTemplate.convertAndSend(endpoint, returnValue)
        return returnValue
    }

    companion object: KLogging()
}