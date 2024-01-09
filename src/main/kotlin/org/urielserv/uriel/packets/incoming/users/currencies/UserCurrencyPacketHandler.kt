package org.urielserv.uriel.packets.incoming.users.currencies

import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.PacketHandler
import org.urielserv.uriel.packets.outgoing.users.currencies.UserCreditsPacket
import org.urielserv.uriel.packets.outgoing.users.currencies.UserCurrencyPacket
import java.nio.ByteBuffer

class UserCurrencyPacketHandler : PacketHandler {

    override suspend fun handle(client: UrielServerClient, packet: ByteBuffer) {
        val habbo = client.habbo ?: return

        val habboCreditsCurrency = habbo.currencies.getByName("credits")
        if (habboCreditsCurrency != null) {
            UserCreditsPacket(habboCreditsCurrency.amount).send(client)
        }

        UserCurrencyPacket(habbo).send(client)
    }

}