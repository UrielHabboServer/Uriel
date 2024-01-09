package org.urielserv.uriel.packets.incoming.users.inventory.badges

import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.PacketHandler
import org.urielserv.uriel.packets.outgoing.users.inventory.badges.UserBadgesPacket
import java.nio.ByteBuffer

class UserBadgesPacketHandler : PacketHandler {

    override suspend fun handle(client: UrielServerClient, packet: ByteBuffer) {
        val habbo = client.habbo ?: return

        UserBadgesPacket(habbo).send(habbo)
    }

}