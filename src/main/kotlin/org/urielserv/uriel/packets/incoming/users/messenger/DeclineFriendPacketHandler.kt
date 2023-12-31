package org.urielserv.uriel.packets.incoming.users.messenger

import org.urielserv.uriel.extensions.getBoolean
import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.PacketHandler
import java.nio.ByteBuffer

class DeclineFriendPacketHandler : PacketHandler {

    override suspend fun handle(client: UrielServerClient, packet: ByteBuffer) {
        val habbo = client.habbo ?: return

        val declineAll = packet.getBoolean()

        if (declineAll) {
            habbo.messenger.friendshipRequests.forEach { it.decline() }

            return
        }

        val declineAmount = packet.getInt()

        for (i in 0..<declineAmount) {
            val userId = packet.getInt()

            habbo.messenger.getFriendshipRequest(userId)?.decline()
        }
    }

}