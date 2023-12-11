package org.urielserv.uriel.packets.outgoing.rooms

import org.urielserv.uriel.packets.outgoing.Outgoing
import org.urielserv.uriel.packets.outgoing.Packet

class RoomPaintPacket(
    private val type: String,
    private val paint: String
) : Packet() {

    override val packetId = Outgoing.RoomPaint

    override suspend fun construct() {
        appendString(type)
        appendString(paint)
    }

}