package org.urielserv.uriel.extensions

import org.urielserv.uriel.TickLoop

val averageTps: Double
    get() {
        if (TickLoop.tickStartTimes.isEmpty()) {
            return 0.0
        }

        val earliestTickTime = TickLoop.tickStartTimes.minByOrNull { it.key }?.value ?: return 0.0
        val latestTickTime = TickLoop.tickStartTimes.maxByOrNull { it.key }?.value ?: return 0.0

        val elapsedTimeInSeconds = (latestTickTime - earliestTickTime) / 1000.0
        val totalTicks = TickLoop.tickStartTimes.size

        return totalTicks / elapsedTimeInSeconds
    }

val averageMspt: Double
    get() {
        if (TickLoop.tickDurations.isEmpty()) {
            return 0.0
        }

        val totalMspt = TickLoop.tickDurations.values.sum().toDouble()
        val totalTicks = TickLoop.tickDurations.size.toDouble()

        return totalMspt / totalTicks
    }