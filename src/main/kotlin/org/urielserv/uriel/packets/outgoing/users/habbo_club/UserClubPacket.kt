package org.urielserv.uriel.packets.outgoing.users.habbo_club

import org.urielserv.uriel.HotelSettings
import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.game.habbos.subscriptions.Subscription
import org.urielserv.uriel.packets.outgoing.OutgoingPacketIDs
import org.urielserv.uriel.packets.outgoing.Packet
import kotlin.math.ceil
import kotlin.math.floor

class UserClubPacket(
    private val habbo: Habbo,
    private val subscriptionType: String,
    private val responseType: Int
) : Packet() {

    override val packetId = OutgoingPacketIDs.UserClub

    override suspend fun construct() {
        appendString(subscriptionType.lowercase()) // Product name

        if (!habbo.subscriptions.hasActiveSubscription(subscriptionType)) {
            appendNoSubscriptionInfo()
        } else {
            appendSubscriptionInfo(habbo.subscriptions.getSubscription(subscriptionType))
        }
    }

    private fun appendNoSubscriptionInfo() {
        repeat(4) {
            appendInt(0)
        }
        appendBoolean(false)
        appendBoolean(false)
        repeat(4) {
            appendInt(0)
        }
    }

    private fun appendSubscriptionInfo(subscription: Subscription?) {
        val remainingTime = subscription?.remainingSeconds ?: 0

        appendInt(calculateDays(remainingTime)) // Days to period end
        appendInt(0) // Member periods
        appendInt(0) // Periods subscribed ahead
        appendInt(determineResponseType(responseType, remainingTime)) // Response type
        appendBoolean(hasEverBeenMember()) // Has ever been member
        appendBoolean(true) // Is VIP
        appendInt(0) // Past club days
        appendInt(calculateDays(0)) // Past VIP days
        appendInt(calculateMinutes(remainingTime)) // Minutes until expiration
        appendInt(1000) // TODO: Minutes since last modified, figure out what it is
    }

    private fun calculateDays(time: Int) = floor(time / 86400.0).toInt().takeIf { it >= 1 } ?: 1

    private fun calculateMinutes(time: Int) = ceil(time / 60.0).toInt()

    private fun hasEverBeenMember(): Boolean = false // TODO: Implement

    private fun determineResponseType(responseType: Int, timeRemaining: Int): Int =
        if (
            responseType == RESPONSE_TYPE_LOGIN
            && timeRemaining > 0 && HotelSettings.habboClub.discountEnabled
            && calculateDays(timeRemaining) <= HotelSettings.habboClub.discountDaysBeforeEnd
        ) RESPONSE_TYPE_DISCOUNT_AVAILABLE else responseType

    companion object {

        const val RESPONSE_TYPE_LOGIN = 1
        const val RESPONSE_TYPE_PURCHASE = 2
        const val RESPONSE_TYPE_DISCOUNT_AVAILABLE = 3
        const val RESPONSE_TYPE_CITIZENSHIP_DISCOUNT = 4

    }

}