package org.urielserv.uriel.packets.incoming.rooms

import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.PacketHandler
import org.urielserv.uriel.packets.outgoing.navigator.NavigatorCategoriesPacket
import java.nio.ByteBuffer

class GetUserFlatCatsPacketHandler : PacketHandler {

    override suspend fun handle(client: UrielServerClient, packet: ByteBuffer) {
        val habbo = client.habbo ?: return

        NavigatorCategoriesPacket(habbo).send(client)
    }

}