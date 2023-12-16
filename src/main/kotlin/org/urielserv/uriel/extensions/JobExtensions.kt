package org.urielserv.uriel.extensions

import org.urielserv.uriel.HotelTickLoop
import org.urielserv.uriel.game.rooms.Room
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * Schedules a job to be executed after a specified delay.
 *
 * @param delay The delay before executing the task.
 * @param task The task to be executed.
 */
fun schedule(delay: Duration, task: suspend () -> Unit) =
    HotelTickLoop.scheduleJob(delay, task)

/**
 * Schedules a job to be executed after a specified delay.
 * The job will be scheduled in the specified room's tick loop.
 *
 * @param room The room to schedule the job in.
 * @param delay The delay before executing the task.
 * @param task The task to be executed.
 */
fun schedule(room: Room, delay: Duration, task: suspend () -> Unit) =
    room.tickLoop?.scheduleJob(delay, task)

/**
 * Schedules a recurring job to be executed at specified intervals.
 *
 * @param interval The interval between subsequent executions of the task.
 *                 The duration is specified in milliseconds.
 * @param delay The delay before the first execution of the task.
 *              The duration is specified in milliseconds.
 * @param task A function representing the task to be executed.
 *             This function takes no arguments and has no return value.
 */
fun scheduleRepeating(interval: Duration, delay: Duration = 0.seconds, task: suspend () -> Unit) =
    HotelTickLoop.scheduleRepeatingJob(delay, interval, task)

/**
 * Schedules a recurring job to be executed at specified intervals.
 * The job will be scheduled in the specified room's tick loop.
 *
 * @param room The room to schedule the job in.
 * @param interval The interval between subsequent executions of the task.
 *                 The duration is specified in milliseconds.
 * @param delay The delay before the first execution of the task.
 *              The duration is specified in milliseconds.
 *  @param task A function representing the task to be executed.
 *              This function takes no arguments and has no return value.
 */
fun scheduleRepeating(room: Room, interval: Duration, delay: Duration = 0.seconds, task: suspend () -> Unit) =
    room.tickLoop?.scheduleRepeatingJob(delay, interval, task)
