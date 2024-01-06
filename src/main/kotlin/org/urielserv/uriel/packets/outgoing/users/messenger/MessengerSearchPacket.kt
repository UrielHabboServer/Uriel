package org.urielserv.uriel.packets.outgoing.users.messenger

import org.urielserv.uriel.game.habbos.HabboInfo
import org.urielserv.uriel.packets.outgoing.Outgoing
import org.urielserv.uriel.packets.outgoing.Packet

class MessengerSearchPacket(
    private val unknownUsers: List<HabboInfo>,
    private val friendUsers: List<HabboInfo>
) : Packet() {

    override val packetId = Outgoing.MessengerSearch

    override suspend fun construct() {
        appendInt(friendUsers.size)
        for (habboInfo in friendUsers) {
            appendInt(habboInfo.id)
            appendString(habboInfo.username)
            appendString(habboInfo.motto)
            appendBoolean(false)
            appendBoolean(false)
            appendString("")
            appendInt(1)
            appendString(habboInfo.look)
            appendString("")
        }

        appendInt(unknownUsers.size)
        for (habboInfo in unknownUsers) {
            appendInt(habboInfo.id)
            appendString(habboInfo.username)
            appendString(habboInfo.motto)
            appendBoolean(false)
            appendBoolean(false)
            appendString("")
            appendInt(1)
            appendString(if (habboInfo.isOnline) habboInfo.look else "")
            appendString("")
        }
    }

}