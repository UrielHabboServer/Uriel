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
import org.urielserv.uriel.game.habbos.messenger.HabboMessenger
import org.urielserv.uriel.game.habbos.notifications.HabboNotifications
import org.urielserv.uriel.game.habbos.room_unit.HabboRoomUnit
import org.urielserv.uriel.game.habbos.subscriptions.HabboSubscriptions
import org.urielserv.uriel.game.rooms.Room
import org.urielserv.uriel.networking.UrielServerClient

/**
 * Represents a Habbo user.
 */
class Habbo internal constructor(
    val info: HabboInfo,
    val client: UrielServerClient
) {

    val data = loadData()

    val currencies = HabboCurrencies(this)
    val inventory = HabboInventory(this)
    val subscriptions = HabboSubscriptions(this)
    val messenger = HabboMessenger(this)

    val notifications = HabboNotifications(this)

    var room: Room? = null
        internal set
    var roomUnit: HabboRoomUnit? = null
        internal set

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
        client.dispose()

        info.isOnline = false
        info.lastOnline = currentUnixSeconds
        info.flushChanges()

        room?.leave(this)

        currencies.unload()
        messenger.unload()

        HabboManager.unloadHabbo(this)
    }

}