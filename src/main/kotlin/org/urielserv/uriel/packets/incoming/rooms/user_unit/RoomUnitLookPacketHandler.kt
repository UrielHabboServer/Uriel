package org.urielserv.uriel.packets.incoming.rooms.user_unit

import org.urielserv.uriel.extensions.readInt
import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.PacketHandler
import java.io.ByteArrayInputStream

class RoomUnitLookPacketHandler : PacketHandler {

    override suspend fun handle(client: UrielServerClient, packet: ByteArrayInputStream) {
        if (client.habbo == null) return

        if (client.habbo!!.room == null) return

        val x = packet.readInt()
        val y = packet.readInt()

        val tile = client.habbo!!.room!!.tileMap!!.getTile(x, y) ?: return

        client.habbo!!.roomUnit!!.lookAt(tile)
    }

}