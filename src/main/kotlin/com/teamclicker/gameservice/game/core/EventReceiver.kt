package com.teamclicker.gameservice.game.core

import ch.qos.logback.classic.Level
import com.teamclicker.gameservice.extensions.KLogging
import com.teamclicker.gameservice.extensions.round
import kotlin.system.measureTimeMillis

class EventReceiver(
    threadId: Int,
    fps: Int,
    loadBufferSize: Int
) : Thread("EventReceiver-$threadId") {
    internal val tickRate: Long
    @Volatile
    var isRunning = true
    /**
     * Array of n recent load values expressed in percents
     * When execution time = 0ms then entity is 0
     * When execution time = the tickRate value then entity is 1
     * When execution time > the tickRate value then entity is > 1
     */
    internal val recentLoadBuffer: Array<Float>
    internal var loopCounter: Long = 0

    internal var taskQueue = arrayListOf<() -> Unit>()

    init {
        require(fps > 0) { "fps must be greater than 0, but is $fps" }
        tickRate = 1000L / fps

        require(loadBufferSize > 0) { "loadBufferSize must be greater than 0, but is $loadBufferSize" }
        recentLoadBuffer = Array(loadBufferSize) { 0f }
    }

    override fun run() {
        while (isRunning) {
            loopCounter++
            val executionTime = measureTimeMillis {
                if (taskQueue.isEmpty()) {
                    return@measureTimeMillis
                }
                val tasks = taskQueue.take(taskQueue.size)
                taskQueue = ArrayList(taskQueue.drop(taskQueue.size))

                executeTasks(tasks)
            }
            /* When tasks take a bit more time to execute
             * we don't want to wait another full frame.
              * We instead would assume that a portion of the sleep time
              * has already been consumed by the execution time*/
            val sleepTime = tickRate - executionTime
            saveLoadEntity(executionTime)
            /* We don't want to sleep when there is no time for it */
            if (sleepTime <= 0L) {
                logger.trace { "[${loopCounter}] Execution time took ${executionTime}ms. Average load: ${countAverageLoad()}%. No time to sleep" }
                continue
            }
            logger.trace { "[${loopCounter}] Execution time took ${executionTime}ms. Average load: ${countAverageLoad()}%. Sleeping for ${sleepTime}ms..." }
            sleep(sleepTime)
        }
    }

    fun queueTask(task: () -> Unit) = taskQueue.add(task)

    fun countAverageLoad() = (recentLoadBuffer.average() * 100).round(2)

    internal fun executeTasks(tasks: List<() -> Unit>) {
        for (task in tasks) {
            task()
        }
    }

    internal fun saveLoadEntity(executionTime: Long) {
        require(executionTime >= 0) { "Execution time cannot be < than 0" }
        val index = (loopCounter % recentLoadBuffer.size).toInt()
        recentLoadBuffer[index] = executionTime.toFloat() / tickRate
    }

    companion object: KLogging(Level.TRACE)
}