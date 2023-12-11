package org.urielserv.uriel.packets.outgoing.rooms

import org.urielserv.uriel.packets.outgoing.Outgoing
import org.urielserv.uriel.packets.outgoing.Packet

class CanCreateRoomPacket(
    private val canCreateRoom: Boolean,
    private val roomLimit: Int
) : Packet() {

    override val packetId = Outgoing.CanCreateRoom

    override suspend fun construct() {
        appendInt(if (canCreateRoom) 0 else 1)
        appendInt(roomLimit)
    }

}