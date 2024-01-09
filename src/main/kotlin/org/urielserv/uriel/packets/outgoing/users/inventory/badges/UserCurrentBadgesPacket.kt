package org.urielserv.uriel.packets.outgoing.users.inventory.badges

import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.game.habbos.HabboInfo
import org.urielserv.uriel.game.habbos.inventory.badges.Badge
import org.urielserv.uriel.packets.outgoing.Outgoing
import org.urielserv.uriel.packets.outgoing.Packet

class UserCurrentBadgesPacket(
    private val habboInfo: HabboInfo,
    private val badges: List<Badge>
) : Packet() {

    override val packetId = Outgoing.UserCurrentBadges

    constructor(habbo: Habbo) : this(habbo.info, habbo.inventory.badges.activeBadges)

    override suspend fun construct() {
        appendInt(habboInfo.id)

        appendInt(badges.size)
        for (badge in badges) {
            appendInt(badge.slotId)
            appendString(badge.code)
        }
    }

}