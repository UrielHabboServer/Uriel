package org.urielserv.uriel.game.habbos

import org.urielserv.uriel.game.habbos.inventory.HabboInventory
import org.urielserv.uriel.game.habbos.subscriptions.HabboSubscriptions
import org.urielserv.uriel.networking.UrielServerClient

/**
 * Represents a Habbo user.
 *
 * @param id The unique identifier of the Habbo user.
 * @param info The information of the Habbo user.
 */
class Habbo(
    val id: Int,
    val info: HabboInfo,
) {

    var client: UrielServerClient? = null

    val inventory = HabboInventory(this)
    val subscriptions = HabboSubscriptions(this)

    init {
        info.habbo = this
    }

}