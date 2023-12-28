package org.urielserv.uriel.packets.outgoing.users.notifications

import org.urielserv.uriel.packets.outgoing.Packet

class InClientLinkPacket(
    private val link: String
) : Packet() {

    override val packetId = 2023

    override suspend fun construct() {
        appendString(link)
    }

}