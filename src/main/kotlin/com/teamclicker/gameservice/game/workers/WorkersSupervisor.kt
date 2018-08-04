package com.teamclicker.gameservice.game.workers

import com.teamclicker.gameservice.extensions.KLogging
import com.teamclicker.gameservice.game.core.EventReceiver
import com.teamclicker.gameservice.game.core.EventTransmitter

class WorkersSupervisor(
    fps: Double,
    maxAdditionalThreads: Int = Runtime.getRuntime().availableProcessors() - 1
//    val workerSpawnThreshold: Double = 90.0,
//    val spawnBreakTime: Long = 10_000 // 10s
) : Thread("WorkersSupervisor") {
    internal val tickRate: Long

    internal var eventReceivers = arrayListOf<EventReceiver>()
    internal var eventTransmitters = arrayListOf<EventTransmitter>()

    init {
        require(fps > 0) { "fps must be greater than 0, but is $fps" }
        tickRate = (1000 / fps).toLong()

        require(maxAdditionalThreads > 0) { "maxAdditionalThreads must be greater than 0, but is $maxAdditionalThreads" }

        spawnEventReceiver()
        spawnEventTransmitter()
    }

    /**
     * TODO implement proper algorithms
     */
    fun getEventReceiver(): EventReceiver {
        return eventReceivers[0]
    }

    /**
     * TODO implement proper algorithms
     */
    fun getEventTransmitter(): EventTransmitter {
        return eventTransmitters[0]
    }

//    override fun run() {
//        while (true) {
//            logger.trace { "Tick" }
//            sleep(tickRate)
//        }
//    }

//    fun getEventReceiver(strategy: GetWorkerStrategy = RANDOM): AbstractTaskWorker {
//        synchronized(eventReceivers) {
//            when (strategy) {
//                RANDOM -> {
//                    val index = (0..eventReceivers.size).random()
//                    return eventReceivers[index]
//                }
//                else -> TODO()
//            }
//        }
//
//    }

    private fun spawnEventReceiver() {
        eventReceivers.add(EventReceiver(EventReceiver.counter++, 5, 200))
    }

    private fun spawnEventTransmitter() {
        eventTransmitters.add(EventTransmitter(EventTransmitter.counter++, 5, 200))
    }

    companion object : KLogging()
}
