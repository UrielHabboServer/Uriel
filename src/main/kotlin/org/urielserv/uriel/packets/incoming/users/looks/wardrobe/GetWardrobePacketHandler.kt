package org.urielserv.uriel.packets.incoming.users.looks.wardrobe

import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.PacketHandler
import org.urielserv.uriel.packets.outgoing.users.looks.UserWardrobePagePacket
import java.io.ByteArrayInputStream

class GetWardrobePacketHandler : PacketHandler {

    override suspend fun handle(client: UrielServerClient, packet: ByteArrayInputStream) {
        if (client.habbo == null) return

        UserWardrobePagePacket(client.habbo!!).send(client)
    }

}