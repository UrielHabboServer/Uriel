package org.urielserv.uriel.packets.outgoing.navigator

import org.urielserv.uriel.NavigatorManager
import org.urielserv.uriel.packets.outgoing.Outgoing
import org.urielserv.uriel.packets.outgoing.Packet

class NavigatorMetadataPacket : Packet() {

    override val packetId = Outgoing.NavigatorMetadata

    override suspend fun construct() {
        val tabs = NavigatorManager.getTabs()

        appendInt(tabs.size)
        for (tab in tabs) {
            appendString(tab.id)
            appendInt(0)
        }
    }

}