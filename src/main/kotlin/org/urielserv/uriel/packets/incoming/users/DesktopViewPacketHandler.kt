package org.urielserv.uriel.packets.incoming.users

import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.PacketHandler
import java.io.ByteArrayInputStream

class DesktopViewPacketHandler : PacketHandler {

    override suspend fun handle(client: UrielServerClient, packet: ByteArrayInputStream) {
        if (client.habbo == null) return

        if (client.habbo!!.room == null) return

        client.habbo!!.room!!.leave(client.habbo!!)
    }

}