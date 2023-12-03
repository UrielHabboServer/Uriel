package org.urielserv.uriel.packets.incoming.navigator

import org.urielserv.uriel.extensions.readString
import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.PacketHandler
import java.io.ByteArrayInputStream

class NavigatorSearchPacketHandler : PacketHandler {

    override suspend fun handle(client: UrielServerClient, packet: ByteArrayInputStream) {
        if (client.habbo == null) return

        var searchCode = packet.readString()
        val data = packet.readString()

        if (searchCode == "query" || searchCode == "groups") {
            searchCode = "hotel_view"
        }


    }

}