package org.urielserv.uriel.game.habbos.subscriptions

import org.ktorm.dsl.eq
import org.ktorm.entity.add
import org.ktorm.entity.filter
import org.ktorm.entity.forEach
import org.urielserv.uriel.Database
import org.urielserv.uriel.core.database.schemas.users.UserSubscriptionsSchema
import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.packets.outgoing.users.subscriptions.UserSubscriptionPacket

@Suppress("unused")
class HabboSubscriptions(
    val habbo: Habbo
) : Collection<Subscription> {

    private val _subscriptions = mutableListOf<Subscription>()

    override val size: Int
        get() = _subscriptions.size

    init {
        Database.sequenceOf(UserSubscriptionsSchema)
            .filter { it.userId eq habbo.info.id }
            .forEach { _subscriptions.add(it) }

        for (subscription in _subscriptions) {
            UserSubscriptionPacket(habbo, subscription, UserSubscriptionPacket.RESPONSE_TYPE_LOGIN).sendSync(habbo)
        }
    }

    fun checkIfExpired() {
        _subscriptions.forEach { it.checkIfExpired() }
    }

    fun hasActiveHabboClubMembership(): Boolean = hasActiveSubscription("habbo_club")

    fun hasActiveSubscription(subscriptionType: String): Boolean =
        _subscriptions.any { it.type == subscriptionType && it.isActive }

    fun getHabboClubSubscription(): Subscription? = getSubscription("habbo_club")

    fun getSubscription(subscriptionType: String): Subscription? =
        _subscriptions.firstOrNull { it.type == subscriptionType }

    fun getActiveSubscription(subscriptionType: String): Subscription? =
        _subscriptions.firstOrNull { it.type == subscriptionType && it.isActive }

    fun registerSubscription(subscription: Subscription) {
        _subscriptions.add(subscription)

        Database.sequenceOf(UserSubscriptionsSchema)
            .add(subscription)
    }

    override fun iterator(): Iterator<Subscription> {
        return _subscriptions.iterator()
    }

    override fun isEmpty(): Boolean {
        return _subscriptions.isEmpty()
    }

    override fun containsAll(elements: Collection<Subscription>): Boolean {
        return _subscriptions.containsAll(elements)
    }

    override fun contains(element: Subscription): Boolean {
        return _subscriptions.contains(element)
    }

}