package org.urielserv.uriel.packets.incoming.users

import org.urielserv.uriel.HabboManager
import org.urielserv.uriel.extensions.getBoolean
import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.PacketHandler
import org.urielserv.uriel.packets.outgoing.users.UserProfilePacket
import java.nio.ByteBuffer

class UserProfilePacketHandler : PacketHandler {

    override suspend fun handle(client: UrielServerClient, packet: ByteBuffer) {
        val habbo = client.habbo ?: return

        val userId = packet.getInt()
        packet.getBoolean() // No clue what this is, not even Nitro knows

        val habboInfo = HabboManager.getHabboInfo(userId) ?: return

        UserProfilePacket(habbo, habboInfo).send(client)
    }

}