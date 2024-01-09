package org.urielserv.uriel.packets.outgoing.users.inventory.badges

import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.packets.outgoing.Outgoing
import org.urielserv.uriel.packets.outgoing.Packet

class UserBadgesPacket(
    private val habbo: Habbo
) : Packet() {

    override val packetId = Outgoing.UserBadges

    override suspend fun construct() {
        val badges = habbo.inventory.badges

        appendInt(badges.size)
        for (badge in badges) {
            appendInt(badge.id)
            appendString(badge.code)
        }

        appendInt(badges.activeBadges.size)
        for (badge in badges.activeBadges) {
            appendInt(badge.slotId)
            appendString(badge.code)
        }
    }

}