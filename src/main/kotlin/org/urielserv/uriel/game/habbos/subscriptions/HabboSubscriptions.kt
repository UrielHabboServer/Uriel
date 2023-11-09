package org.urielserv.uriel.game.habbos.subscriptions

import org.ktorm.database.iterator
import org.ktorm.dsl.eq
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import org.urielserv.uriel.Database
import org.urielserv.uriel.database.schemas.UsersSubscriptions
import org.urielserv.uriel.game.habbos.Habbo

@Suppress("unused")
class HabboSubscriptions(
    val habbo: Habbo
) {

    private val subscriptions = mutableListOf<Subscription>()

    init {
        Database.from(UsersSubscriptions)
            .select()
            .where(UsersSubscriptions.userId eq habbo.id)
            .rowSet
            .iterator()
            .forEach { row ->
                subscriptions.add(
                    Subscription(
                        row[UsersSubscriptions.id]!!,
                        habbo,
                        row[UsersSubscriptions.subscriptionType]!!,
                        row[UsersSubscriptions.subscriptionStart]!!,
                        row[UsersSubscriptions.subscriptionEnd]!!,
                        row[UsersSubscriptions.isActive]!!.toBoolean()
                    )
                )
            }
    }

    fun checkIfExpired() {
        subscriptions.forEach { it.checkIfExpired() }
    }

    fun hasActiveHabboClubMembership(): Boolean = hasActiveSubscription("HABBO_CLUB")

    fun hasActiveSubscription(subscriptionType: String): Boolean =
        subscriptions.any { it.subscriptionType == subscriptionType && it.isActive }

    fun getSubscription(subscriptionType: String): Subscription? =
        subscriptions.firstOrNull { it.subscriptionType == subscriptionType }

    fun getActiveSubscription(subscriptionType: String): Subscription? =
        subscriptions.firstOrNull { it.subscriptionType == subscriptionType && it.isActive }

    fun registerSubscription(subscription: Subscription) {
        subscriptions.add(subscription)

        Database.insert(UsersSubscriptions) {
            it.userId to habbo.id
            it.subscriptionType to subscription.subscriptionType
            it.subscriptionStart to subscription.subscriptionStart
            it.subscriptionEnd to subscription.subscriptionEnd
            it.isActive to subscription.isActive
        }
    }

}