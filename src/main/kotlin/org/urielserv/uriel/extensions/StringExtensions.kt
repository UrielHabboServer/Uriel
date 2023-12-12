package org.urielserv.uriel.extensions

import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

/**
 * Converts a string representation of duration into a Duration object.
 *
 * The string representation of duration should follow the format: "XdYhZmWs",
 * where X represents the number of days, Y represents the number of hours,
 * Z represents the number of minutes, and W represents the number of seconds.
 *
 * @return the Duration object representing the parsed duration
 * @throws IllegalArgumentException if the string representation of duration is invalid
 */
fun String.toDuration(): Duration {
    val regex = Regex("(\\d+)([dDhHmMsS])")
    val matches = regex.findAll(this)

    var duration = Duration.ZERO

    for (match in matches) {
        val (value, unit) = match.destructured
        val intValue = value.toLong()

        duration += when (unit.lowercase()) {
            "d" -> intValue.days
            "h" -> intValue.hours
            "m" -> intValue.minutes
            "s" -> intValue.seconds
            else -> throw IllegalArgumentException("Unsupported time unit: $unit")
        }
    }

    return duration
}