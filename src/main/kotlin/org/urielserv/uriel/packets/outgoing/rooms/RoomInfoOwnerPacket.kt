package org.urielserv.uriel.packets.outgoing.rooms

import org.urielserv.uriel.game.rooms.Room
import org.urielserv.uriel.packets.outgoing.Outgoing
import org.urielserv.uriel.packets.outgoing.Packet

class RoomInfoOwnerPacket(
    private val room: Room,
    private val isOwner: Boolean
) : Packet() {

    override val packetId = Outgoing.RoomInfoOwner

    override suspend fun construct() {
        appendInt(room.info.id)
        appendBoolean(isOwner)
    }

}