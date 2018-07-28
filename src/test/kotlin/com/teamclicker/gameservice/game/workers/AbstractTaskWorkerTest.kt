package com.teamclicker.gameservice.game.workers

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

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
            for (i in 1..100_000) {
                sut.queueTask { foo++ }
                sut.queueTaskEveryTick { foo++ }
            }

            assertEquals(100_000, sut.taskQueue.size)
            assertEquals(100_000, sut.everyTickTaskQueue.size)

            sut.run()

            assertEquals(0, sut.taskQueue.size)
            assertEquals(100_000, sut.everyTickTaskQueue.size)
            assert(sut.recentLoadBuffer[0] > 0)
            println(sut.recentLoadBuffer[0])
        }

        @Test
        fun `should work when queues are empty`() {
            assertEquals(0, sut.taskQueue.size)
            assertEquals(0, sut.everyTickTaskQueue.size)

            sut.run()

            assertEquals(0, sut.taskQueue.size)
            assertEquals(0, sut.everyTickTaskQueue.size)
        }

        @Test
        fun `should not go to sleep when execution time was too long`() {
            sut.queueTask { Thread.sleep(30) }


            sut.run()

            assertEquals(false, sleepInvoked)
        }

    }
}