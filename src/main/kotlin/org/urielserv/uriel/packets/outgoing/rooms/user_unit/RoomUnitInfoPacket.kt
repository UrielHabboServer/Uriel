package org.urielserv.uriel.packets.outgoing.rooms.user_unit

import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.packets.outgoing.Outgoing
import org.urielserv.uriel.packets.outgoing.Packet

class RoomUnitInfoPacket(
    private val habbo: Habbo
) : Packet() {

    override val packetId = Outgoing.RoomUnitInfo

    override suspend fun construct() {
        appendInt(if (habbo.roomUnit == null) -1 else habbo.roomUnit!!.id)
        appendString(habbo.info.look)
        appendString(habbo.info.gender.short())
        appendString(habbo.info.motto)
        appendInt(0) // TODO: Achievement score
    }

}