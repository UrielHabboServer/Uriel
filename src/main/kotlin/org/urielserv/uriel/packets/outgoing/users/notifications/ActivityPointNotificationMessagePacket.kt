package org.urielserv.uriel.packets.outgoing.users.notifications

import org.urielserv.uriel.packets.outgoing.Outgoing
import org.urielserv.uriel.packets.outgoing.Packet

class ActivityPointNotificationMessagePacket(
    private val amount: Int,
    private val amountChanged: Int,
    private val type: Int
) : Packet() {

    override val packetId = Outgoing.ActivityPointNotification

    override suspend fun construct() {
        appendInt(amount)
        appendInt(amountChanged)
        appendInt(type)
    }

}