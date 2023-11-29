package org.urielserv.uriel.packets.outgoing.users

import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.packets.outgoing.OutgoingPacketIDs
import org.urielserv.uriel.packets.outgoing.Packet

class UserDataPacket(
    private val habbo: Habbo
) : Packet() {

    override val packetId = OutgoingPacketIDs.UserData

    override suspend fun construct() {
        appendInt(habbo.info.id)
        appendString(habbo.info.username)
        appendString(habbo.info.look)
        appendString(habbo.info.gender.name)
        appendString(habbo.info.motto)
        appendString(habbo.info.username)
        appendBoolean(false)
        appendInt(0) // respect points received
        appendInt(0) // available respect points
        appendInt(0) // available pet respect points
        appendBoolean(false)
        appendString("01-01-1970 00:00:00")
        appendBoolean(false) // allow name change
        appendBoolean(false)
    }

}