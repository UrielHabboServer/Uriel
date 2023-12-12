package org.urielserv.uriel.game.habbos

import org.urielserv.uriel.HabboManager
import org.urielserv.uriel.extensions.currentUnixSeconds
import org.urielserv.uriel.game.habbos.currencies.HabboCurrencies
import org.urielserv.uriel.game.habbos.inventory.HabboInventory
import org.urielserv.uriel.game.habbos.subscriptions.HabboSubscriptions
import org.urielserv.uriel.game.rooms.Room
import org.urielserv.uriel.networking.UrielServerClient

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

    val notifications = HabboNotifications(this)

    var room: Room? = null
    var roomState: HabboRoomState? = null

    suspend fun disconnect() {
        info.isOnline = false
        info.lastOnline = currentUnixSeconds

        info.flushChanges()

        unload()
    }

    private suspend fun unload() {
        client?.dispose()
        HabboManager.unloadHabbo(this)

        currencies.unload()
    }

}