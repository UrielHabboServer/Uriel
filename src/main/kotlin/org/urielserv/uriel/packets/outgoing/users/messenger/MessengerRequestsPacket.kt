package org.urielserv.uriel.packets.outgoing.users.messenger

import org.urielserv.uriel.game.habbos.messenger.interfaces.FriendshipRequest
import org.urielserv.uriel.packets.outgoing.Outgoing
import org.urielserv.uriel.packets.outgoing.Packet

class MessengerRequestsPacket(
    private val requests: List<FriendshipRequest>
) : Packet() {

    override val packetId = Outgoing.MessengerRequests

    override suspend fun construct() {
        appendInt(requests.size)
        appendInt(requests.size)

        requests.forEach {
            appendInt(it.senderHabboInfo.id)
            appendString(it.senderHabboInfo.username)
            appendString(it.senderHabboInfo.look)
        }
    }

}