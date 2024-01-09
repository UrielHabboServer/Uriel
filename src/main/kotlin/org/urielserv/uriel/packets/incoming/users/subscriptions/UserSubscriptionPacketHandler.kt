package org.urielserv.uriel.packets.incoming.users.subscriptions

import org.urielserv.uriel.extensions.getString
import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.PacketHandler
import org.urielserv.uriel.packets.outgoing.users.subscriptions.UserSubscriptionPacket
import java.nio.ByteBuffer

class UserSubscriptionPacketHandler : PacketHandler {

    override suspend fun handle(client: UrielServerClient, packet: ByteBuffer) {
        val habbo = client.habbo ?: return

        val subscriptionType = packet.getString()
        val subscription = habbo.subscriptions.getSubscription(subscriptionType) ?: return

        UserSubscriptionPacket(habbo, subscription, UserSubscriptionPacket.RESPONSE_TYPE_NORMAL)
            .send(client)
    }

}