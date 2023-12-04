package org.urielserv.uriel.packets.outgoing.navigator

import org.urielserv.uriel.packets.outgoing.Outgoing
import org.urielserv.uriel.packets.outgoing.Packet

class NavigatorLiftedPacket : Packet() {

    override val packetId = Outgoing.NavigatorLifted

    override suspend fun construct() {
        appendInt(0)
    }

}