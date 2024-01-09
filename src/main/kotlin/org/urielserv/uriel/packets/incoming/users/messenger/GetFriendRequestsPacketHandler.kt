package org.urielserv.uriel.packets.incoming.users.messenger

import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.PacketHandler
import org.urielserv.uriel.packets.outgoing.users.messenger.MessengerRequestsPacket
import java.nio.ByteBuffer

class GetFriendRequestsPacketHandler : PacketHandler {

    override suspend fun handle(client: UrielServerClient, packet: ByteBuffer) {
        val habbo = client.habbo ?: return

        MessengerRequestsPacket(habbo.messenger.friendshipRequests).send(client)
    }

}