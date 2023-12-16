package org.urielserv.uriel.game.habbos

import org.ktorm.dsl.eq
import org.ktorm.entity.add
import org.ktorm.entity.find
import org.urielserv.uriel.Database
import org.urielserv.uriel.HabboManager
import org.urielserv.uriel.core.database.schemas.users.UserDataSchema
import org.urielserv.uriel.extensions.currentUnixSeconds
import org.urielserv.uriel.game.habbos.currencies.HabboCurrencies
import org.urielserv.uriel.game.habbos.inventory.HabboInventory
import org.urielserv.uriel.game.habbos.room_unit.HabboRoomUnit
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

    val data = loadData()

    val currencies = HabboCurrencies(this)
    val inventory = HabboInventory(this)
    val subscriptions = HabboSubscriptions(this)

    val notifications = HabboNotifications(this)

    internal var room: Room? = null
    internal var roomUnit: HabboRoomUnit? = null

    private fun loadData(): HabboData {
        val data = Database.sequenceOf(UserDataSchema)
            .find { it.id eq info.id }

        if (data == null) {
            Database.sequenceOf(UserDataSchema)
                .add(HabboData {
                    habboInfo = info
                })

            return loadData()
        } else {
            return data
        }
    }

    suspend fun disconnect() {
        info.isOnline = false
        info.lastOnline = currentUnixSeconds
        info.flushChanges()

        room?.leave(this)

        unload()
    }

    private suspend fun unload() {
        client?.dispose()
        HabboManager.unloadHabbo(this)

        currencies.unload()
    }

}