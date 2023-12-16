package org.urielserv.uriel.packets.outgoing.rooms.user_unit

import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.packets.outgoing.Outgoing
import org.urielserv.uriel.packets.outgoing.Packet

class RoomUnitRemovePacket(
    private val habbo: Habbo
) : Packet() {

    override val packetId = Outgoing.RoomUnitRemove

    override suspend fun construct() {
        appendString(habbo.roomUnit!!.id.toString())
    }

}