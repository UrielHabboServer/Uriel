package org.urielserv.uriel.packets.incoming.users

import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.PacketHandler
import org.urielserv.uriel.packets.outgoing.users.UserInfoPacket
import java.nio.ByteBuffer

class UserInfoPacketHandler : PacketHandler {

    override suspend fun handle(client: UrielServerClient, packet: ByteBuffer) {
        /*
        Packets to add:
        - UserDataComposer
        - UserPerksComposer
        - UserClientSettings
         */

        if (client.habbo == null) return

        UserInfoPacket(client.habbo!!).send(client)
    }

}