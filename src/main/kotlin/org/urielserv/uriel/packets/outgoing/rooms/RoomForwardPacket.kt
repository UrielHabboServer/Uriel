package org.urielserv.uriel.packets.outgoing.rooms

import org.urielserv.uriel.game.rooms.Room
import org.urielserv.uriel.packets.outgoing.Outgoing
import org.urielserv.uriel.packets.outgoing.Packet

class RoomForwardPacket(
    private val roomId: Int
) : Packet() {

    override val packetId = Outgoing.RoomForward

    constructor(room: Room) : this(room.info.id)

    override suspend fun construct() {
        appendInt(roomId)
    }

}