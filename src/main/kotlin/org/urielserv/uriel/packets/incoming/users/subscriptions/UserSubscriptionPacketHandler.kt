package org.urielserv.uriel.packets.incoming.users.subscriptions

import org.urielserv.uriel.extensions.getString
import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.PacketHandler
import org.urielserv.uriel.packets.outgoing.users.subscriptions.UserSubscriptionPacket
import java.nio.ByteBuffer

class UserSubscriptionPacketHandler : PacketHandler {

    override suspend fun handle(client: UrielServerClient, packet: ByteBuffer) {
        if (client.habbo == null) return

        val subscriptionType = packet.getString()
        val subscription = client.habbo!!.subscriptions.getSubscription(subscriptionType) ?: return

        UserSubscriptionPacket(client.habbo!!, subscription, UserSubscriptionPacket.RESPONSE_TYPE_NORMAL)
            .send(client)
    }

}