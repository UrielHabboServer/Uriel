package org.urielserv.uriel.packets.outgoing.navigator

import org.urielserv.uriel.packets.outgoing.OutgoingPacketIDs
import org.urielserv.uriel.packets.outgoing.Packet

class NavigatorEventCategories : Packet() {

    override val packetId = OutgoingPacketIDs.NavigatorEventCategories

    override suspend fun construct() {
        appendInt(0)
    }

}