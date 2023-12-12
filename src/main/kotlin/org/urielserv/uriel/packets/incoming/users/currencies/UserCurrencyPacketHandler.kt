package org.urielserv.uriel.packets.incoming.users.currencies

import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.PacketHandler
import org.urielserv.uriel.packets.outgoing.users.currencies.UserCreditsPacket
import org.urielserv.uriel.packets.outgoing.users.currencies.UserCurrencyPacket
import java.io.ByteArrayInputStream

class UserCurrencyPacketHandler : PacketHandler {

    override suspend fun handle(client: UrielServerClient, packet: ByteArrayInputStream) {
        if (client.habbo == null) return

        val habboCreditsCurrency = client.habbo!!.currencies.getByName("credits")
        if (habboCreditsCurrency != null) {
            UserCreditsPacket(habboCreditsCurrency.amount).send(client)
        }

        UserCurrencyPacket(client.habbo!!).send(client)
    }

}