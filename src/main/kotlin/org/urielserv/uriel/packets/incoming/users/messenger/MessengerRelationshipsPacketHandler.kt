package org.urielserv.uriel.packets.incoming.users.messenger

import org.urielserv.uriel.HabboManager
import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.PacketHandler
import org.urielserv.uriel.packets.outgoing.users.messenger.MessengerRelationshipsPacket
import java.nio.ByteBuffer

class MessengerRelationshipsPacketHandler : PacketHandler {

    override suspend fun handle(client: UrielServerClient, packet: ByteBuffer) {
        client.habbo ?: return

        val targetHabboId = packet.getInt()
        val targetHabboInfo = HabboManager.getHabboInfo(targetHabboId) ?: return

        MessengerRelationshipsPacket(targetHabboInfo).send(client)
    }

}