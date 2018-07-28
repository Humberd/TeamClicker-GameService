package com.teamclicker.gameservice.game.workers

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class WorkerConfiguration {

    @Bean
    fun initWorkersSupervisor(): WorkersSupervisor {
        return WorkersSupervisor(
            fps = 5.0
        ).also { it.start() }
    }
}