package org.urielserv.uriel.packets.outgoing.rooms.user_unit.chat

import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.packets.outgoing.Outgoing
import org.urielserv.uriel.packets.outgoing.Packet

class RoomUnitTypingPacket(
    private val habbo: Habbo,
    private val isTyping: Boolean
) : Packet() {

    override val packetId = Outgoing.RoomUnitTyping

    override suspend fun construct() {
        appendInt(habbo.roomUnit!!.id)
        appendInt(if (isTyping) 1 else 0)
    }

}