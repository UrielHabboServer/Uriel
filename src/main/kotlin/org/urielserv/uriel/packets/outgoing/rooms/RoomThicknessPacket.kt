package org.urielserv.uriel.packets.outgoing.rooms

import org.urielserv.uriel.game.rooms.Room
import org.urielserv.uriel.packets.outgoing.Outgoing
import org.urielserv.uriel.packets.outgoing.Packet

class RoomThicknessPacket(
    private val room: Room
) : Packet() {

    override val packetId = Outgoing.RoomThickness

    override suspend fun construct() {
        appendBoolean(room.info.areWallsHidden)
        appendInt(room.info.wallThickness)
        appendInt(room.info.floorThickness)
    }

}