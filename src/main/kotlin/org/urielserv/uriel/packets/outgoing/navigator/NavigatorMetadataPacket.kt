package org.urielserv.uriel.packets.outgoing.navigator

import org.urielserv.uriel.packets.outgoing.Outgoing
import org.urielserv.uriel.packets.outgoing.Packet

class NavigatorMetadataPacket : Packet() {

    override val packetId = Outgoing.NavigatorMetadata

    override suspend fun construct() {
        appendInt(4)
        appendString("official_view")
        appendInt(0)
        appendString("hotel_view")
        appendInt(0)
        appendString("roomads_view")
        appendInt(0)
        appendString("myworld_view")
        appendInt(0)
    }

}