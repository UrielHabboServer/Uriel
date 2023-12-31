package org.urielserv.uriel.packets.outgoing.users.notifications

import org.urielserv.uriel.packets.outgoing.Outgoing
import org.urielserv.uriel.packets.outgoing.Packet

class InClientLinkPacket(
    private val link: String
) : Packet() {

    override val packetId = Outgoing.InClientLink

    override suspend fun construct() {
        appendString(link)
    }

}