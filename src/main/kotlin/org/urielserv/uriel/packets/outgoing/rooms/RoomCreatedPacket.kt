package org.urielserv.uriel.packets.outgoing.rooms

import org.urielserv.uriel.packets.outgoing.Outgoing
import org.urielserv.uriel.packets.outgoing.Packet

class RoomCreatedPacket(
    private val roomId: Int,
    private val roomName: String
) : Packet() {

    override val packetId = Outgoing.RoomCreated

    override suspend fun construct() {
        appendInt(roomId)
        appendString(roomName)
    }

}