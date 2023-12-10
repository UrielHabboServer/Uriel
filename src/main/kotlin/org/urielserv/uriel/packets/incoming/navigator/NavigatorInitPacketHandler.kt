package org.urielserv.uriel.packets.incoming.navigator

import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.PacketHandler
import org.urielserv.uriel.packets.outgoing.navigator.*
import java.io.ByteArrayInputStream

class NavigatorInitPacketHandler : PacketHandler {

    override suspend fun handle(client: UrielServerClient, packet: ByteArrayInputStream) {
        if (client.habbo == null) return

        NavigatorMetadataPacket().send(client)
        NavigatorEventCategories().send(client)
    }

}