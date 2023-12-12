package org.urielserv.uriel.game.habbos.subscriptions

import org.ktorm.dsl.eq
import org.ktorm.entity.add
import org.ktorm.entity.filter
import org.ktorm.entity.forEach
import org.urielserv.uriel.Database
import org.urielserv.uriel.core.database.schemas.users.UserSubscriptionsSchema
import org.urielserv.uriel.game.habbos.Habbo

@Suppress("unused")
class HabboSubscriptions(
    val habbo: Habbo
) {

    private val subscriptions = mutableListOf<Subscription>()

    init {
        Database.sequenceOf(UserSubscriptionsSchema)
            .filter { it.userId eq habbo.info.id }
            .forEach { subscriptions.add(it) }
    }

    fun checkIfExpired() {
        subscriptions.forEach { it.checkIfExpired() }
    }

    fun hasActiveHabboClubMembership(): Boolean = hasActiveSubscription("HABBO_CLUB")

    fun hasActiveSubscription(subscriptionType: String): Boolean =
        subscriptions.any { it.type == subscriptionType && it.isActive }

    fun getSubscription(subscriptionType: String): Subscription? =
        subscriptions.firstOrNull { it.type == subscriptionType }

    fun getActiveSubscription(subscriptionType: String): Subscription? =
        subscriptions.firstOrNull { it.type == subscriptionType && it.isActive }

    fun registerSubscription(subscription: Subscription) {
        subscriptions.add(subscription)

        Database.sequenceOf(UserSubscriptionsSchema)
            .add(Subscription {
                habboInfo = this@HabboSubscriptions.habbo.info
                type = subscription.type
                start = subscription.start
                end = subscription.end
                isActive = subscription.isActive
            })
    }

}