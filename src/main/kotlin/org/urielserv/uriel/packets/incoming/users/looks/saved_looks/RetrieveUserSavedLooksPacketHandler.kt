package org.urielserv.uriel.packets.incoming.users.looks.saved_looks

import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.PacketHandler
import org.urielserv.uriel.packets.outgoing.users.looks.UserSavedLooksPacket
import java.io.ByteArrayInputStream

class RetrieveUserSavedLooksPacketHandler : PacketHandler {

    override suspend fun handle(client: UrielServerClient, packet: ByteArrayInputStream) {
        if (client.habbo == null) return

        UserSavedLooksPacket(client.habbo!!).send(client)
    }

}