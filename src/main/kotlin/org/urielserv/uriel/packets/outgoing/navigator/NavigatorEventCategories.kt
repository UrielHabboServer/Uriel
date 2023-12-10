package org.urielserv.uriel.packets.outgoing.navigator

import org.urielserv.uriel.packets.outgoing.Outgoing
import org.urielserv.uriel.packets.outgoing.Packet

class NavigatorEventCategories : Packet() {

    override val packetId = Outgoing.NavigatorEventCategories

    override suspend fun construct() {
        appendInt(0)

        // TODO: Implement
    }

}