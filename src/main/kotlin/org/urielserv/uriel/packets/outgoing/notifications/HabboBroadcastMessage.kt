package org.urielserv.uriel.packets.outgoing.rooms

import org.urielserv.uriel.packets.outgoing.Outgoing
import org.urielserv.uriel.packets.outgoing.Packet

class HabboBroadcastMessage(
    private val message: String
) : Packet() {

    override val packetId = Outgoing.GenericAlert

    override suspend fun construct() {
        appendString(message)
    }
}
