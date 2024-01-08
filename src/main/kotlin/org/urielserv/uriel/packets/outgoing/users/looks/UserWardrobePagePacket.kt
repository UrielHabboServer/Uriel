package org.urielserv.uriel.packets.outgoing.users.looks

import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.packets.outgoing.Outgoing
import org.urielserv.uriel.packets.outgoing.Packet

class UserWardrobePagePacket(
    private val habbo: Habbo
) : Packet() {

    override val packetId = Outgoing.UserWardrobePage

    override suspend fun construct() {
        val looks = habbo.inventory.savedLooks.looks

        appendInt(1)
        appendInt(looks.size)
        for (look in looks) {
            appendInt(look.slotId)
            appendString(look.look)
            appendString(look.gender.short())
        }
    }

}