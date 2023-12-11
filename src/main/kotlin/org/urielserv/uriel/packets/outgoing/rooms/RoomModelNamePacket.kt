package org.urielserv.uriel.packets.outgoing.rooms

import org.urielserv.uriel.game.rooms.Room
import org.urielserv.uriel.packets.outgoing.Outgoing
import org.urielserv.uriel.packets.outgoing.Packet

class RoomModelNamePacket(
    private val room: Room
) : Packet() {

    override val packetId = Outgoing.RoomModelName

    override suspend fun construct() {
        appendString(room.info.model.name)
        appendInt(room.info.id)
    }

}