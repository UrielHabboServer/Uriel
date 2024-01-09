package org.urielserv.uriel.packets.incoming.users.messenger

import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.PacketHandler
import java.nio.ByteBuffer

class RemoveFriendPacketHandler : PacketHandler {

    override suspend fun handle(client: UrielServerClient, packet: ByteBuffer) {
        val habbo = client.habbo ?: return

        val removeAmount = packet.getInt()

        for (i in 0..<removeAmount) {
            val userId = packet.getInt()

            habbo.messenger.getFriendship(userId)?.remove()
        }
    }

}