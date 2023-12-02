package org.urielserv.uriel.extensions

import org.urielserv.uriel.HotelTickLoop
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * Schedules a job to be executed after a specified delay.
 *
 * @param delay The delay before executing the task.
 * @param task The task to be executed.
 */
fun schedule(delay: Duration, task: () -> Unit) =
    HotelTickLoop.scheduleJob(delay, task)

/**
 * Schedules a recurring job to be executed at specified intervals.
 *
 * @param interval The interval between subsequent executions of the task.
 *                 The duration is specified in milliseconds.
 * @param start The delay before the first execution of the task.
 *              The duration is specified in milliseconds.
 * @param task A function representing the task to be executed.
 *             This function takes no arguments and has no return value.
 */
fun scheduleRepeating(interval: Duration, start: Duration = 0.seconds, task: () -> Unit) =
    HotelTickLoop.scheduleRepeatingJob(start, interval, task)
