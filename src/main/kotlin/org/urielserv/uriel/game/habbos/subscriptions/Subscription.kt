package org.urielserv.uriel.game.habbos.subscriptions

import org.ktorm.entity.Entity
import org.urielserv.uriel.HotelSettings
import org.urielserv.uriel.extensions.currentUnixSeconds
import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.game.habbos.HabboInfo

interface Subscription : Entity<Subscription> {

    companion object : Entity.Factory<Subscription>()

    val id: Int
    var habboInfo: HabboInfo
    val habbo: Habbo?
        get() = habboInfo.habbo

    var type: String
    var level: Int

    var start: Int
    var end: Int

    var isActive: Boolean

    val hasExpired: Boolean
        get() = currentUnixSeconds > end

    val remainingSeconds: Int
        get() = end - currentUnixSeconds

    fun checkIfExpired() {
        if (!hasExpired) return

        if (HotelSettings.habbos.subscriptions.deleteExpiredSubscriptions) {
            delete()
        } else {
            isActive = false
            flushChanges()
        }
    }

}