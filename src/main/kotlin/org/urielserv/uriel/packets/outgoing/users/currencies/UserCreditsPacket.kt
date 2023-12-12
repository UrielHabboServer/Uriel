package org.urielserv.uriel.packets.outgoing.users.currencies

import org.urielserv.uriel.packets.outgoing.Outgoing
import org.urielserv.uriel.packets.outgoing.Packet

class UserCreditsPacket(
    private val amount: Int
) : Packet() {

    override val packetId = Outgoing.UserCredits

    override suspend fun construct() {
        appendString("$amount.0")
    }

}