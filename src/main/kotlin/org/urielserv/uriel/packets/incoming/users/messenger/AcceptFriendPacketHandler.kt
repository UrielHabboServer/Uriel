package org.urielserv.uriel.packets.incoming.users.messenger

import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.PacketHandler
import java.nio.ByteBuffer

class AcceptFriendPacketHandler : PacketHandler {

    override suspend fun handle(client: UrielServerClient, packet: ByteBuffer) {
        if (client.habbo == null) return

        val habbo = client.habbo!!

        val requestAmount = packet.getInt()

        for (i in 0..<requestAmount) {
            val userId = packet.getInt()

            habbo.messenger.getFriendshipRequest(userId)?.accept()
        }
    }

}