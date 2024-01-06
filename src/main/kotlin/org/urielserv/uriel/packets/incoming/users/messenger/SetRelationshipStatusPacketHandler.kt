package org.urielserv.uriel.packets.incoming.users.messenger

import org.urielserv.uriel.MessengerManager
import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.PacketHandler
import org.urielserv.uriel.packets.outgoing.users.messenger.MessengerUpdatePacket
import java.nio.ByteBuffer

class SetRelationshipStatusPacketHandler : PacketHandler {

    override suspend fun handle(client: UrielServerClient, packet: ByteBuffer) {
        if (client.habbo == null) return

        val habbo = client.habbo!!

        val targetId = packet.getInt()
        val relationshipId = packet.getInt()

        val relationship = MessengerManager.getRelationship(relationshipId) ?: return
        val friendship = habbo.messenger.getFriendship(targetId) ?: return

        friendship.setRelationship(habbo.info, relationship)
        friendship.flushChanges()

        MessengerUpdatePacket(habbo, MessengerUpdatePacket.Action.UPDATE to friendship).send(client)
    }

}