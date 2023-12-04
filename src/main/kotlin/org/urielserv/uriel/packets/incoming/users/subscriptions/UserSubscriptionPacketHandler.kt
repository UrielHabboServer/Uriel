package org.urielserv.uriel.packets.incoming.users.subscriptions

import org.urielserv.uriel.extensions.readString
import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.PacketHandler
import org.urielserv.uriel.packets.outgoing.users.subscriptions.UserSubscriptionPacket
import java.io.ByteArrayInputStream

class UserSubscriptionPacketHandler : PacketHandler {

    override suspend fun handle(client: UrielServerClient, packet: ByteArrayInputStream) {
        if (client.habbo == null) return

        val subscriptionType = packet.readString()

        UserSubscriptionPacket(client.habbo!!, subscriptionType, UserSubscriptionPacket.RESPONSE_TYPE_NORMAL).send(
            client
        )
    }

}