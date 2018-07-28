package com.teamclicker.gameservice.game.workers

import ch.qos.logback.classic.Level
import com.teamclicker.gameservice.extensions.KLogging
import com.teamclicker.gameservice.extensions.round
import kotlin.system.measureTimeMillis

abstract class AbstractTaskWorker(
    name: String,
    fps: Int,
    loadBufferCapacity: Int
): Thread(name) {
    internal val tickRate: Long
    @Volatile
    var isRunning = true
    /**
     * Recent execution times expressed in percents
     * 0 = 0%
     * 1 = 100%,
     * where 100% is the [tickRate]
     */
    internal val recentLoadBuffer: Array<Float>
    internal var loopCounter: Long = -1

    /**
     * The list of tasks that are queued to be executed in the next tick.
     * After execution the tasks are removed.
     */
    internal var taskQueue = arrayListOf<() -> Unit>()
    /**
     * The list of tasks that are queued to be executed every tick.
     * After execution the tasks are NOT removed.
     */
    internal var everyTickTaskQueue = arrayListOf<() -> Unit>()

    init {
        require(fps > 0) { "fps must be greater than 0, but is $fps" }
        tickRate = 1000L / fps

        require(loadBufferCapacity > 0) { "loadBufferCapacity must be greater than 0, but is $loadBufferCapacity" }
        recentLoadBuffer = Array(loadBufferCapacity) { 0f }

        isDaemon = true
    }

    override fun run() {
        while (isRunning) {
            loopCounter++
            val executionTime = measureTimeMillis {
                val tasks = taskQueue.toList()
                taskQueue = ArrayList(taskQueue.drop(tasks.size))

                executeTasks(tasks)
                executeTasks(everyTickTaskQueue.toList())
            }
            /* When tasks take a bit more time to execute
             * we don't want to wait another full frame.
              * We instead would assume that a portion of the sleep time
              * has already been consumed by the execution time*/
            val sleepTime = tickRate - executionTime
            saveExecutionTime(executionTime)
            /* We don't want to sleep when there is no time for it */
            if (sleepTime <= 0L) {
                logger.trace { "[${loopCounter}] Execution time took ${executionTime}ms. Average load: ${countAverageLoad()}%. No time to sleep" }
                continue
            }
            logger.trace { "[${loopCounter}] Execution time took ${executionTime}ms. Average load: ${countAverageLoad()}%. Sleeping for ${sleepTime}ms..." }
            goSleep(sleepTime)
        }
    }

    open internal fun goSleep(time: Long) = sleep(time)

    fun queueTask(task: () -> Unit) = taskQueue.add(task)

    fun queueTaskEveryTick(task: () -> Unit) = everyTickTaskQueue.add(task)

    /**
     * Count average load and rounds it to 2 decimal places
     */
    fun countAverageLoad() = recentLoadBuffer.average().round(2).toFloat()

    internal fun executeTasks(tasks: List<() -> Unit>) {
        for (task in tasks) {
            task()
        }
    }

    /**
     * Saves execution time as a percent of the tick rate
     * in the next position of the array
     */
    open internal fun saveExecutionTime(executionTime: Long) {
        require(executionTime >= 0) { "Execution time cannot be < than 0" }
        val index = (loopCounter % recentLoadBuffer.size).toInt()
        recentLoadBuffer[index] = executionTime.toFloat() / tickRate
    }

    companion object: KLogging(Level.DEBUG)
}