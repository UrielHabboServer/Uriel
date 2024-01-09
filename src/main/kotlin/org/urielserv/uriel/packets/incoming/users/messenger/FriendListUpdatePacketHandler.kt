package org.urielserv.uriel.packets.incoming.users.messenger

import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.PacketHandler
import java.nio.ByteBuffer

class FriendListUpdatePacketHandler : PacketHandler {

    override suspend fun handle(client: UrielServerClient, packet: ByteBuffer) {
        client.habbo ?: return

        // TODO: Implement
    }

}