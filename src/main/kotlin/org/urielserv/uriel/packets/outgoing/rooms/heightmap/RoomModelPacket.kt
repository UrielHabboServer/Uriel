package org.urielserv.uriel.packets.outgoing.rooms.heightmap

import org.urielserv.uriel.game.rooms.Room
import org.urielserv.uriel.packets.outgoing.Outgoing
import org.urielserv.uriel.packets.outgoing.Packet

class RoomModelPacket(
    private val room: Room
) : Packet() {

    override val packetId = Outgoing.RoomModel

    override suspend fun construct() {
        appendBoolean(true)
        appendInt(room.info.wallHeight)
        appendString(room.info.model.heightmap.replace("\n", "\r"))
    }

}