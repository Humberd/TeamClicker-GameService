package com.teamclicker.gameservice.game.spring

import com.teamclicker.gameservice.game.workers.WorkersSupervisor
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