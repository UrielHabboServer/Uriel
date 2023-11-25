package org.urielserv.uriel.game.habbos.subscriptions

import org.ktorm.dsl.eq
import org.urielserv.uriel.Database
import org.urielserv.uriel.HotelSettings
import org.urielserv.uriel.database.enums.Bool
import org.urielserv.uriel.database.schemas.UserSubscriptionsSchema
import org.urielserv.uriel.game.habbos.Habbo

data class Subscription(
    val id: Int,
    val habbo: Habbo,

    val subscriptionType: String,

    val subscriptionStart: Int,
    val subscriptionEnd: Int,

    var isActive: Boolean
) {

    fun isExpired(): Boolean = (System.currentTimeMillis() / 1000).toInt() > subscriptionEnd

    fun checkIfExpired() {
        if (!isExpired()) return

        isActive = false

        if (HotelSettings.habbos.subscription.deleteExpiredSubscriptions) {
            Database.delete(UserSubscriptionsSchema) {
                it.id eq id
            }
        } else {
            Database.update(UserSubscriptionsSchema) {
                it.isActive to Bool.FALSE
                where {
                    it.id eq id
                }
            }
        }
    }

}