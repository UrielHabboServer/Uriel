package org.urielserv.uriel.packets.outgoing.navigator

import org.urielserv.uriel.packets.outgoing.OutgoingPacketIDs
import org.urielserv.uriel.packets.outgoing.Packet

class NavigatorLiftedPacket : Packet() {

    override val packetId = OutgoingPacketIDs.NavigatorLifted

    override suspend fun construct() {
        appendInt(0)
    }

}