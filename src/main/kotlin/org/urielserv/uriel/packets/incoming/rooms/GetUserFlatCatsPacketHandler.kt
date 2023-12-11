package org.urielserv.uriel.packets.incoming.rooms

import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.PacketHandler
import org.urielserv.uriel.packets.outgoing.navigator.NavigatorCategoriesPacket
import java.io.ByteArrayInputStream

class GetUserFlatCatsPacketHandler : PacketHandler {

    override suspend fun handle(client: UrielServerClient, packet: ByteArrayInputStream) {
        if (client.habbo == null) return

        NavigatorCategoriesPacket(client.habbo!!).send(client)
    }

}