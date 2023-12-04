package org.urielserv.uriel.packets.outgoing.users

import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.packets.outgoing.Outgoing
import org.urielserv.uriel.packets.outgoing.Packet

class UserInfoPacket(
    private val habbo: Habbo
) : Packet() {

    override val packetId = Outgoing.UserInfo

    override suspend fun construct() {
        appendInt(habbo.info.id)
        appendString(habbo.info.username)
        appendString(habbo.info.look)
        appendString(habbo.info.gender.name)
        appendString(habbo.info.motto)
        appendString(habbo.info.username)
        appendBoolean(false) // directMail
        appendInt(0) // respectsReceived
        appendInt(0) // respectsRemaining
        appendInt(0) // respectsPetRemaining
        appendBoolean(false) // streamPublishingAllowed
        appendString("01-01-1970 00:00:00") // lastAccessDate
        appendBoolean(false) // canChangeName
        appendBoolean(false) // safetyLocked
    }

}