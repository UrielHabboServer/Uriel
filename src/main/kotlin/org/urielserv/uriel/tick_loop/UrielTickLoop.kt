package org.urielserv.uriel.tick_loop

import org.urielserv.uriel.tick_loop.jobs.Job
import org.urielserv.uriel.tick_loop.jobs.RepeatingJob
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.time.Duration

class UrielTickLoop(private val ticksPerSecond: Int) {

    private val executor = Executors.newSingleThreadScheduledExecutor()

    private var ticks = 0
    private var totalTicks = 0L
    var startTime = System.currentTimeMillis()
        private set

    private var currentJobCount = 0
    private val jobs = mutableListOf<Job>()
    private val repeatingJobs = mutableListOf<RepeatingJob>()

    var averageTps = 0.0
        private set
    var averageMspt = 0.0
        private set

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
        totalTicks++

        jobs.filter { it.start == ticks }.forEach { it.run() }

        repeatingJobs.filter { it.start == ticks || (it.start < ticks && it.interval > 0 && (ticks - it.start) % it.interval == 0) }.forEach {
            if (!it.isCancelled) {
                it.run()
            } else {
                repeatingJobs.remove(it)
            }
        }

        val elapsed = System.currentTimeMillis() - start
        updateStatistics(elapsed)
    }

    private fun updateStatistics(elapsed: Long) {
        val elapsedSeconds = elapsed.toDouble() / 1000.0
        averageTps = totalTicks / elapsedSeconds
        averageMspt = elapsed / totalTicks.toDouble()
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

}