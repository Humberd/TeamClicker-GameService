package com.teamclicker.gameservice.game.workers

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.concurrent.TimeUnit.MILLISECONDS

internal class AbstractTaskWorkerTest {

    @Nested
    inner class Constructor {
        @Test
        fun `should throw error when fps is less than 0`() {
            assertThrows(IllegalArgumentException::class.java) {
                object : AbstractTaskWorker("foo", -5, 30) {
                }
            }
        }

        @Test
        fun `should throw error when fps equals 0`() {
            assertThrows(IllegalArgumentException::class.java) {
                object : AbstractTaskWorker("foo", 0, 30) {
                }
            }
        }

        @Test
        fun `should throw error when loadBufferCapacity is less than 0`() {
            assertThrows(IllegalArgumentException::class.java) {
                object : AbstractTaskWorker("foo", 25, -30) {
                }
            }
        }

        @Test
        fun `should throw error when loadBufferCapacity equals 0`() {
            assertThrows(IllegalArgumentException::class.java) {
                object : AbstractTaskWorker("foo", 25, 0) {
                }
            }
        }

        @Test
        fun `should set name`() {
            val sut = object : AbstractTaskWorker("NewThread-Name", 25, 23) {

            }

            assertEquals("NewThread-Name", sut.name)
        }

        @Test
        fun `should be daemon`() {
            val sut = object : AbstractTaskWorker("NewThread-Name", 25, 23) {

            }

            assertEquals(true, sut.isDaemon)
        }
    }

    @Nested
    inner class CountAverageLoad {
        @Test
        fun `should count average load`() {
            val sut = object : AbstractTaskWorker("foo", 5, 10) {

            }
            // sum = 2.752
            arrayOf(1f, 0.4f, 0.2f, 0.6f, 0.552f).forEachIndexed { index, fl ->
                sut.recentLoadBuffer[index] = fl
            }
            // the capacity i 10, but we provided only 5 values, so the average is 0.2752
            val result = sut.countAverageLoad()

            assertEquals(0.28f, result)

        }
    }

    @Nested
    inner class SaveExecutionTime {
        lateinit var sut: AbstractTaskWorker

        @BeforeEach
        fun setUp() {
            sut = object : AbstractTaskWorker("foo", 5, 10) {
            }
        }

        @Test
        fun `should not save time when it's less than 0`() {
            assertThrows(IllegalArgumentException::class.java) {
                sut.saveExecutionTime(-2)
            }
        }

        @Test
        fun `should save time to 0th index`() {
            sut.loopCounter = 0
            sut.saveExecutionTime(50)

            assertEquals(0.25f, sut.recentLoadBuffer[0])
        }

        @Test
        fun `should save time to 9th index`() {
            sut.loopCounter = 9
            sut.saveExecutionTime(160)

            assertEquals(0.8f, sut.recentLoadBuffer[9])
        }

        @Test
        fun `should save time to 1st index`() {
            sut.loopCounter = 11
            sut.saveExecutionTime(200)

            assertEquals(1f, sut.recentLoadBuffer[1])
        }
    }

    @Nested
    inner class Run {
        lateinit var sut: AbstractTaskWorker
        var sleepInvoked = false

        @BeforeEach
        fun setUp() {
            sleepInvoked = false
            sut = object : AbstractTaskWorker("foo", 200, 10) {
                override fun goSleep(time: Long) {
                    sleepInvoked = true
                }

                override fun saveExecutionTime(executionTime: Long) {
                    this.isRunning = false
                    super.saveExecutionTime(executionTime)
                }
            }
        }

        @Test
        fun `should empty taskQueue every loop`() {
            var foo = 0
            for (i in 1..10) {
                sut.queueTask { foo++; Thread.sleep(1) }
                sut.queueTaskEveryTick { foo++ }
            }

            assertEquals(10, sut.taskQueue.size)
            assertEquals(10, sut.fixedTickRateTaskQueue.size)

            sut.run()

            assertEquals(0, sut.taskQueue.size)
            assertEquals(10, sut.fixedTickRateTaskQueue.size)
            assert(sut.recentLoadBuffer[0] > 0)
        }

        @Test
        fun `should work when queues are empty`() {
            assertEquals(0, sut.taskQueue.size)
            assertEquals(0, sut.fixedTickRateTaskQueue.size)

            sut.run()

            assertEquals(0, sut.taskQueue.size)
            assertEquals(0, sut.fixedTickRateTaskQueue.size)
        }

        @Test
        fun `should not go to sleep when execution time was too long`() {
            sut.queueTask { Thread.sleep(30) }


            sut.run()

            assertEquals(false, sleepInvoked)
        }
    }

    @Nested
    inner class QueueTaskEveryTick {
        lateinit var sut: AbstractTaskWorker

        @BeforeEach
        fun setUp() {
            sut = object : AbstractTaskWorker("foo", 10, 10) {
            }
        }

        @Test
        fun `should add task with tick rate interval of 1`() {
            sut.queueTaskEveryTick { }

            assertEquals(1, sut.fixedTickRateTaskQueue[0].tickRateInterval)
        }
    }

    @Nested
    inner class RemoveTaskEveryTick {
        lateinit var sut: AbstractTaskWorker

        @BeforeEach
        fun setUp() {
            sut = object : AbstractTaskWorker("foo", 5, 10) {
            }
        }

        @Test
        fun `should remove all every tick tasks`() {
            val task1 = {

            }
            val task2 = {

            }

            sut.queueTaskEveryTick(task1)
            sut.queueTaskEveryTick(task1)
            sut.queueTaskEveryTick(task2)

            assertEquals(3, sut.fixedTickRateTaskQueue.size)

            sut.removeTaskEveryTick(task2)

            assertEquals(2, sut.fixedTickRateTaskQueue.size)

            sut.removeTaskEveryTick(task1)

            assertEquals(0, sut.fixedTickRateTaskQueue.size)

        }
    }

    @Nested
    inner class QueueFixedRateTaskEvery {
        lateinit var sut: AbstractTaskWorker

        @Test
        fun `should queue task every 4th tick`() {
            sut = object : AbstractTaskWorker("foo", 10, 10) {}

            sut.queueFixedRateTaskEvery(400, MILLISECONDS) { }

            /* Tick is every 100 ms and we want to invoke every 400 ms */
            assertEquals(1, sut.fixedTickRateTaskQueue.size)
            assertEquals(4, sut.fixedTickRateTaskQueue[0].tickRateInterval)
        }

        @Test
        fun `should round tick interval up when it's above 50% of tickRate time`() {
            sut = object : AbstractTaskWorker("foo", 10, 10) {}

            sut.queueFixedRateTaskEvery(399, MILLISECONDS) { }

            /* Tick is every 100 ms and we want to invoke every 399 ms */
            assertEquals(1, sut.fixedTickRateTaskQueue.size)
            assertEquals(4, sut.fixedTickRateTaskQueue[0].tickRateInterval)
        }

        @Test
        fun `should round tick interval down when it's below 50% of tickRate time`() {
            sut = object : AbstractTaskWorker("foo", 10, 10) {}

            sut.queueFixedRateTaskEvery(349, MILLISECONDS) { }

            /* Tick is every 100 ms and we want to invoke every 399 ms */
            assertEquals(1, sut.fixedTickRateTaskQueue.size)
            assertEquals(3, sut.fixedTickRateTaskQueue[0].tickRateInterval)
        }

        @Test
        fun `should round tick interval up when it's exactly 50% of tickRate time`() {
            sut = object : AbstractTaskWorker("foo", 10, 10) {}

            sut.queueFixedRateTaskEvery(350, MILLISECONDS) { }

            /* Tick is every 100 ms and we want to invoke every 399 ms */
            assertEquals(1, sut.fixedTickRateTaskQueue.size)
            assertEquals(4, sut.fixedTickRateTaskQueue[0].tickRateInterval)
        }
    }

    @Nested
    inner class RemoveFixedRateTask {
        lateinit var sut: AbstractTaskWorker

        @BeforeEach
        fun setUp() {
            sut = object : AbstractTaskWorker("foo", 5, 10) {}
        }

        @Test
        fun `should remove all tasks`() {
            val task1 = {

            }
            val task2 = {

            }

            sut.queueFixedRateTaskEvery(1, false, task1)
            sut.queueFixedRateTaskEvery(1, false, task2)
            sut.queueFixedRateTaskEvery(1, false, task2)

            assertEquals(3, sut.fixedTickRateTaskQueue.size)

            sut.removeFixedRateTask(task1)

            assertEquals(2, sut.fixedTickRateTaskQueue.size)

            sut.removeFixedRateTask(task2)

            assertEquals(0, sut.fixedTickRateTaskQueue.size)

            sut.removeFixedRateTask(task1)

            assertEquals(0, sut.fixedTickRateTaskQueue.size)
        }
    }

    @Nested
    inner class ExecuteFixedRateTasks {
        lateinit var sut: AbstractTaskWorker

        @BeforeEach
        fun setUp() {
            sut = object : AbstractTaskWorker("foo", 5, 10) {}
        }

        @Test
        fun `should execute task with 1 tick rate every tick`() {
            var task1Counter = 0
            val task1 = AbstractTaskWorker.FixedRateTask(1) {
                task1Counter++
            }

            assertEquals(0, task1Counter)

            sut.executeFixedRateTasks(listOf(task1))

            assertEquals(1, task1Counter)

            sut.executeFixedRateTasks(listOf(task1))

            assertEquals(2, task1Counter)
        }

        @Test
        fun `should execute task with 2 tick rate every 2nd tick`() {
            var task1Counter = 0
            val task1 = AbstractTaskWorker.FixedRateTask(2) {
                task1Counter++
            }


            assertEquals(0, task1Counter)

            sut.executeFixedRateTasks(listOf(task1)) // 1st invoke
            assertEquals(0, task1Counter)

            sut.executeFixedRateTasks(listOf(task1)) // 2nd invoke
            assertEquals(1, task1Counter)

            sut.executeFixedRateTasks(listOf(task1)) // 3rd invoke
            assertEquals(1, task1Counter)

            sut.executeFixedRateTasks(listOf(task1)) // 4th invoke
            assertEquals(2, task1Counter)
        }

        @Test
        fun `should execute task with 2 tick rate every 2nd tick starting immediately`() {
            var task1Counter = 0
            val task1 = AbstractTaskWorker.FixedRateTask(2, true) {
                task1Counter++
            }


            assertEquals(0, task1Counter)

            sut.executeFixedRateTasks(listOf(task1)) // 1st invoke
            assertEquals(1, task1Counter)

            sut.executeFixedRateTasks(listOf(task1)) // 2nd invoke
            assertEquals(1, task1Counter)

            sut.executeFixedRateTasks(listOf(task1)) // 3rd invoke
            assertEquals(2, task1Counter)

            sut.executeFixedRateTasks(listOf(task1)) // 4th invoke
            assertEquals(2, task1Counter)

            sut.executeFixedRateTasks(listOf(task1)) // 5th invoke
            assertEquals(3, task1Counter)
        }
    }

    @Nested
    inner class FixedRateTaskTest {
        @Test
        fun `should set currentTick to 0 when not starting immediately`() {
            val sut = AbstractTaskWorker.FixedRateTask(32, false) {}

            assertEquals(0, sut.currentTick)
        }

        @Test
        fun `should set currentTick to almost executable wien starting immediately`() {
            val sut = AbstractTaskWorker.FixedRateTask(32, true) {}

            assertEquals(31, sut.currentTick)
        }
    }
}