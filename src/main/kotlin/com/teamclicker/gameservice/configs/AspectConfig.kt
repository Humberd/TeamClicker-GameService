package com.teamclicker.gameservice.configs

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.EnableAspectJAutoProxy

@EnableAspectJAutoProxy
@ComponentScan("com.teamclicker.gameservice.aop")
@Configuration
class AspectConfig {

}