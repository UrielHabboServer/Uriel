package org.urielserv.uriel.packets.outgoing.users.messenger

import org.urielserv.uriel.game.habbos.messenger.interfaces.FriendshipRequest
import org.urielserv.uriel.packets.outgoing.Outgoing
import org.urielserv.uriel.packets.outgoing.Packet

class MessengerRequestPacket(
    private val friendshipRequest: FriendshipRequest
) : Packet() {

    override val packetId = Outgoing.MessengerRequest

    override suspend fun construct() {
        appendInt(friendshipRequest.senderHabboInfo.id)
        appendString(friendshipRequest.senderHabboInfo.username)
        appendString(friendshipRequest.senderHabboInfo.look)
    }

}