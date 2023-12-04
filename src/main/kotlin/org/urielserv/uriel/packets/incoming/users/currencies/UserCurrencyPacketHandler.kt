package org.urielserv.uriel.packets.incoming.users.currencies

import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.PacketHandler
import org.urielserv.uriel.packets.outgoing.users.currencies.UserCreditsPacket
import org.urielserv.uriel.packets.outgoing.users.currencies.UserCurrencyPacket
import java.io.ByteArrayInputStream

class UserCurrencyPacketHandler : PacketHandler {

    override suspend fun handle(client: UrielServerClient, packet: ByteArrayInputStream) {
        if (client.habbo == null) return

        UserCreditsPacket(client.habbo!!).send(client)
        UserCurrencyPacket(client.habbo!!).send(client)
    }

}