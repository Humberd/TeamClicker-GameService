package com.teamclicker.gameservice.game.workers

import com.teamclicker.gameservice.game.core.EventReceiver

class WorkerSupervisor(
    fps: Int,
    maxThreads: Int
) : Thread("WorkerSupervisor") {
    internal val tickRate: Long

    val eventReceivers = arrayListOf<EventReceiver>()

    init {
        require(fps > 0) { "fps must be greater than 0, but is $fps" }
        tickRate = 1000L / fps

        require(maxThreads > 0) { "maxThreads must be greater than 0, but is $maxThreads" }
    }

    override fun run() {
        while (true) {
            doTask()
            sleep(tickRate)
        }
    }

    internal fun doTask() {

    }

//    internal fun wakeUpt
}
