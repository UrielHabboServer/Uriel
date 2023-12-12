package org.urielserv.uriel.packets.outgoing.notifications

import org.urielserv.uriel.packets.outgoing.Outgoing
import org.urielserv.uriel.packets.outgoing.Packet

class HabboBroadcastMessagePacket(
    private val message: String
) : Packet() {

    override val packetId = Outgoing.GenericAlert

    override suspend fun construct() {
        appendString(message)
    }

}
