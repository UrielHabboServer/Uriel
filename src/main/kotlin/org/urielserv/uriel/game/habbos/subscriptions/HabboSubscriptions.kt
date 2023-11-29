package org.urielserv.uriel.game.habbos.subscriptions

import org.ktorm.dsl.eq
import org.ktorm.entity.filter
import org.ktorm.entity.forEach
import org.urielserv.uriel.Database
import org.urielserv.uriel.database.schemas.users.UserSubscriptionsSchema
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

        Database.insert(UserSubscriptionsSchema) {
            set(it.userId, habbo.info.id)
            set(it.subscriptionType, subscription.type)
            set(it.subscriptionStart, subscription.start)
            set(it.subscriptionEnd, subscription.end)
            set(it.isActive, subscription.isActive)
        }
    }

}