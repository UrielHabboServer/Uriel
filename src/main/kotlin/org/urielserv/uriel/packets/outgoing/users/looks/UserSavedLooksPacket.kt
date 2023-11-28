package org.urielserv.uriel.packets.outgoing.users.looks

import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.packets.outgoing.OutgoingPacketIDs
import org.urielserv.uriel.packets.outgoing.Packet

class UserSavedLooksPacket(
    private val habbo: Habbo
) : Packet() {

    override val packetId = OutgoingPacketIDs.UserSavedLooks

    override suspend fun construct() {
        val looks = habbo.inventory.savedLooks.getLooks()

        appendInt(1)
        appendInt(looks.size)
        for (look in looks) {
            appendInt(look.slotId)
            appendString(look.look)
            appendString(look.gender.short())
        }
    }

}