package org.urielserv.uriel.game.habbos.subscriptions

import org.ktorm.entity.Entity
import org.urielserv.uriel.HotelSettings
import org.urielserv.uriel.extensions.currentUnixSeconds
import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.game.habbos.HabboInfo

interface Subscription : Entity<Subscription> {

    val id: Int
    val habboInfo: HabboInfo
    val habbo: Habbo?
        get() = habboInfo.habbo

    val type: String

    val start: Int
    val end: Int

    var isActive: Boolean

    val hasExpired: Boolean
        get() = currentUnixSeconds > end

    val remainingSeconds: Int
        get() = end - currentUnixSeconds

    fun checkIfExpired() {
        if (!hasExpired) return

        if (HotelSettings.habbos.subscription.deleteExpiredSubscriptions) {
            delete()
        } else {
            isActive = false
            flushChanges()
        }
    }

}