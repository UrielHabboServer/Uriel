package org.urielserv.uriel.packets.outgoing.users.messenger

import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.packets.outgoing.Outgoing
import org.urielserv.uriel.packets.outgoing.Packet

class MessengerInvitePacket(
    private val habbo: Habbo,
    private val message: String
) : Packet() {

    override val packetId = Outgoing.MessengerInvite

    override suspend fun construct() {
        appendInt(habbo.info.id)
        appendString(message)
    }

}