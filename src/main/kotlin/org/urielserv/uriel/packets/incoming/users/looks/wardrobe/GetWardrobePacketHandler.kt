package org.urielserv.uriel.packets.incoming.users.looks.wardrobe

import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.PacketHandler
import org.urielserv.uriel.packets.outgoing.users.looks.UserWardrobePagePacket
import java.nio.ByteBuffer

class GetWardrobePacketHandler : PacketHandler {

    override suspend fun handle(client: UrielServerClient, packet: ByteBuffer) {
        val habbo = client.habbo ?: return

        UserWardrobePagePacket(habbo).send(client)
    }

}