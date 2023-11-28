package org.urielserv.uriel.packets.incoming.users

import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.PacketHandler
import org.urielserv.uriel.packets.outgoing.users.UserDataPacket
import java.io.ByteArrayInputStream

class RetrieveUserDataPacketHandler : PacketHandler {

    override suspend fun handle(client: UrielServerClient, packet: ByteArrayInputStream) {
        /*
        Packets to add:
        - UserDataComposer
        - UserPerksComposer
        - UserClientSettings
         */

        if (client.habbo == null) return

        UserDataPacket(client.habbo!!).send(client)
    }

}