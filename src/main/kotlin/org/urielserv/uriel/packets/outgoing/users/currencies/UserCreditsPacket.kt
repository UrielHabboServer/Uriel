package org.urielserv.uriel.packets.outgoing.users.currencies

import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.packets.outgoing.Outgoing
import org.urielserv.uriel.packets.outgoing.Packet

class UserCreditsPacket(
    private val habbo: Habbo
) : Packet() {

    override val packetId = Outgoing.UserCredits

    override suspend fun construct() {
        val currency = habbo.currencies.getByName("credits") ?: return

        appendString("${currency.amount}.0")
    }

}