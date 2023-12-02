package org.urielserv.uriel.packets.incoming.navigator

import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.PacketHandler
import org.urielserv.uriel.packets.outgoing.navigator.*
import java.io.ByteArrayInputStream

class NavigatorInitPacketHandler : PacketHandler {

    override suspend fun handle(client: UrielServerClient, packet: ByteArrayInputStream) {
        if (client.habbo == null) return

        NavigatorSettingsPacket(client.habbo!!).send(client)
        NavigatorMetadataPacket().send(client)
        NavigatorLiftedPacket().send(client)
        NavigatorCollapsedPacket().send(client)
        NavigatorSearchesPacket(client.habbo!!).send(client)
        NavigatorEventCategories().send(client)
    }

}