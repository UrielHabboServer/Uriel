package org.urielserv.uriel.packets.outgoing.users

import org.urielserv.uriel.MessengerManager
import org.urielserv.uriel.extensions.currentUnixSeconds
import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.game.habbos.HabboInfo
import org.urielserv.uriel.packets.outgoing.Outgoing
import org.urielserv.uriel.packets.outgoing.Packet
import java.text.SimpleDateFormat
import java.util.Date

class UserProfilePacket(
    private val viewerHabbo: Habbo,
    private val habboInfo: HabboInfo
) : Packet() {

    override val packetId = Outgoing.UserProfile

    override suspend fun construct() {
        val creationTimestamp = habboInfo.accountCreation
        val creationDate = Date(creationTimestamp * 1000L)
        val creationDateString = SimpleDateFormat("dd-MM-yyyy").format(creationDate)

        appendInt(habboInfo.id)
        appendString(habboInfo.username)
        appendString(habboInfo.look)
        appendString(habboInfo.motto)
        appendString(creationDateString)

        appendInt(0) // TODO: Achievement score

        appendInt(MessengerManager.getFriendshipsByHabboInfo(habboInfo).size)
        appendBoolean(viewerHabbo.messenger.isFriend(habboInfo))
        appendBoolean(viewerHabbo.messenger.hasFriendshipRequest(habboInfo))

        appendBoolean(habboInfo.isOnline)

        appendInt(0) // TODO: Guilds

        appendInt(currentUnixSeconds - if (habboInfo.isOnline) habboInfo.lastLogin else habboInfo.lastOnline)
        appendBoolean(true)
    }

}