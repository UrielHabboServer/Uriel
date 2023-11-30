package org.urielserv.uriel.packets.outgoing.users.looks

import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.packets.outgoing.OutgoingPacketIDs
import org.urielserv.uriel.packets.outgoing.Packet

class UserFigurePacket(
    private val habbo: Habbo
) : Packet() {

    override val packetId = OutgoingPacketIDs.UserFigure

    override suspend fun construct() {
        appendString(habbo.info.look)
        appendString(habbo.info.gender.short())
    }

}