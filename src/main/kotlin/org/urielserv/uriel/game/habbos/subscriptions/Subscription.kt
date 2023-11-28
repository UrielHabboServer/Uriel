package org.urielserv.uriel.game.habbos.subscriptions

import org.ktorm.dsl.eq
import org.urielserv.uriel.Database
import org.urielserv.uriel.HotelSettings
import org.urielserv.uriel.database.schemas.UserSubscriptionsSchema
import org.urielserv.uriel.extensions.currentUnixSeconds
import org.urielserv.uriel.game.habbos.Habbo

data class Subscription(
    val id: Int,
    val habbo: Habbo,

    val type: String,

    val start: Int,
    val end: Int,

    var isActive: Boolean
) {

    val hasExpired: Boolean
        get() = currentUnixSeconds > end

    val remainingSeconds: Int
        get() = end - currentUnixSeconds

    fun checkIfExpired() {
        if (!hasExpired) return

        isActive = false

        if (HotelSettings.habbos.subscription.deleteExpiredSubscriptions) {
            Database.delete(UserSubscriptionsSchema) {
                it.id eq id
            }
        } else {
            Database.update(UserSubscriptionsSchema) {
                set(it.isActive, false)

                where {
                    it.id eq id
                }
            }
        }
    }

}