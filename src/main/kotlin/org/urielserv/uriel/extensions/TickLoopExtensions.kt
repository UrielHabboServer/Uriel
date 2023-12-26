package org.urielserv.uriel.extensions

import org.urielserv.uriel.HotelTickLoop
import org.urielserv.uriel.tick_loop.TickLoop

val TickLoop.averageTps: Double
    get() {
        if (HotelTickLoop.tickStartTimes.isEmpty()) {
            return 0.0
        }

        val earliestTickTime = HotelTickLoop.tickStartTimes.minByOrNull { it.key }?.value ?: return 0.0
        val latestTickTime = HotelTickLoop.tickStartTimes.maxByOrNull { it.key }?.value ?: return 0.0

        val elapsedTimeInSeconds = (latestTickTime - earliestTickTime) / 1000.0
        val totalTicks = HotelTickLoop.tickStartTimes.size

        return totalTicks / elapsedTimeInSeconds
    }

val TickLoop.averageMspt: Double
    get() {
        if (HotelTickLoop.tickDurations.isEmpty()) {
            return 0.0
        }

        val totalMspt = HotelTickLoop.tickDurations.values.sum().toDouble()
        val totalTicks = HotelTickLoop.tickDurations.size.toDouble()

        return totalMspt / totalTicks
    }