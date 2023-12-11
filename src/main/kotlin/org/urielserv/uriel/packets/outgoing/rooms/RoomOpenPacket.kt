package org.urielserv.uriel.packets.outgoing.rooms

import org.urielserv.uriel.packets.outgoing.Outgoing
import org.urielserv.uriel.packets.outgoing.Packet

class RoomOpenPacket : Packet() {

    override val packetId = Outgoing.RoomOpen

    override suspend fun construct() = Unit

}