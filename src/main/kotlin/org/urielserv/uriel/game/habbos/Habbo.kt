package org.urielserv.uriel.game.habbos

import org.urielserv.uriel.game.habbos.currencies.HabboCurrencies
import org.urielserv.uriel.game.habbos.inventory.HabboInventory
import org.urielserv.uriel.game.habbos.subscriptions.HabboSubscriptions
import org.urielserv.uriel.game.rooms.Room
import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.extensions.scheduleRepeating
import org.urielserv.uriel.tick_loop.jobs.RepeatingJob
import kotlin.time.Duration.Companion.seconds

/**
 * Represents a Habbo user.
 *
 * @param info The information of the Habbo user.
 */
class Habbo internal constructor(
    val info: HabboInfo,
) {

    var client: UrielServerClient? = null

    val currencies = HabboCurrencies(this)
    val inventory = HabboInventory(this)
    val subscriptions = HabboSubscriptions(this)

    var room: Room? = null
    var roomState: HabboRoomState? = null

    private val habboTicker: RepeatingJob
    private var ticks = 0

    init {
        habboTicker = scheduleRepeating(1.seconds, 0.seconds) { onTick() }
    }

    fun unload() {
        habboTicker.isCancelled = true
        currencies.unload()
    }

    fun onTick() {
        ticks++

        currencies.onTick(ticks)
    }

}