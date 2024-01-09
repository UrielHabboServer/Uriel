package org.urielserv.uriel.packets.incoming.users

import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.PacketHandler
import java.nio.ByteBuffer

class DesktopViewPacketHandler : PacketHandler {

    override suspend fun handle(client: UrielServerClient, packet: ByteBuffer) {
        val habbo = client.habbo ?: return

        if (habbo.room == null) return

        habbo.room!!.leave(habbo)
    }

}