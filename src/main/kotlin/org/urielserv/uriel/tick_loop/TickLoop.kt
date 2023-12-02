package org.urielserv.uriel.tick_loop

import io.klogging.noCoLogger
import org.urielserv.uriel.tick_loop.jobs.Job
import org.urielserv.uriel.tick_loop.jobs.RepeatingJob
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.time.Duration

class TickLoop(private val ticksPerSecond: Int) {

    private val expectedMaxTimePerTick = 1000 / ticksPerSecond

    private val logger = noCoLogger(TickLoop::class)

    private val executor = Executors.newSingleThreadScheduledExecutor()

    private var ticks = 0
    var startTime = System.currentTimeMillis()
        private set

    private var currentJobCount = 0
    private val jobs = mutableListOf<Job>()
    private val repeatingJobs = mutableListOf<RepeatingJob>()

    val tickStartTimes = mutableMapOf<Int, Long>()
    val tickDurations = mutableMapOf<Int, Long>()

    init {
        executor.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                tick()
            }
        }, 0, 1000 / ticksPerSecond.toLong(), TimeUnit.MILLISECONDS)
    }

    fun tick() {
        val start = System.currentTimeMillis()

        ticks++

        tickStartTimes[ticks] = start

        jobs.filter { it.start == ticks }.forEach {
            if (!it.isCancelled) {
                try {
                    it.run()
                } catch (exc: Exception) {
                    logger.error("Error running Job #${it.id}:")
                    exc.printStackTrace()
                }
            }

            jobs.remove(it)
        }

        repeatingJobs.filter { it.start == ticks || (it.start < ticks && it.interval > 0 && (ticks - it.start) % it.interval == 0) }
            .forEach {
                if (!it.isCancelled) {
                    try {
                        it.run()
                    } catch (exc: Exception) {
                        logger.error("Error running Repeating Job #${it.id}:")
                        exc.printStackTrace()
                    }
                } else {
                    repeatingJobs.remove(it)
                }
            }

        val timeTaken = System.currentTimeMillis() - start

        if (timeTaken > expectedMaxTimePerTick) {
            logger.warn("Tick took ${timeTaken}ms, expected ${expectedMaxTimePerTick}ms")
        }

        tickDurations[ticks] = timeTaken

        checkToClearTickData()
    }

    private fun checkToClearTickData() {
        if (tickStartTimes.size > STATISTICS_CACHE_SIZE) {
            // remove first
            tickStartTimes.remove(tickStartTimes.keys.first())
        }

        if (tickDurations.size > STATISTICS_CACHE_SIZE) {
            // remove first
            tickDurations.remove(tickDurations.keys.first())
        }
    }

    fun end() {
        executor.shutdown()
    }

    private fun createJob(start: Int, task: () -> Unit): Job {
        val job = Job(currentJobCount++, start, task)
        jobs.add(job)
        return job
    }

    private fun createRepeatingJob(start: Int, interval: Int, task: () -> Unit): RepeatingJob {
        val job = RepeatingJob(currentJobCount++, start, interval, task)
        repeatingJobs.add(job)
        return job
    }

    fun scheduleJob(delay: Duration, task: () -> Unit): Job {
        val ticks = (delay.inWholeMilliseconds / (1000 / ticksPerSecond)).toInt()
        return createJob(this.ticks + ticks, task)
    }

    fun scheduleRepeatingJob(delay: Duration, interval: Duration, task: () -> Unit): RepeatingJob {
        val ticks = (delay.inWholeMilliseconds / (1000 / ticksPerSecond)).toInt()
        val intervalTicks = (interval.inWholeMilliseconds / (1000 / ticksPerSecond)).toInt()
        return createRepeatingJob(this.ticks + ticks, intervalTicks, task)
    }

    companion object {

        const val STATISTICS_CACHE_SIZE = 1000

    }

}