package org.urielserv.uriel.packets.incoming.rooms.user_unit

import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.PacketHandler
import java.nio.ByteBuffer

class RoomUnitLookPacketHandler : PacketHandler {

    override suspend fun handle(client: UrielServerClient, packet: ByteBuffer) {
        if (client.habbo == null) return

        if (client.habbo!!.room == null) return

        val x = packet.getInt()
        val y = packet.getInt()

        val tile = client.habbo!!.room!!.tileMap!!.getTile(x, y) ?: return

        if (client.habbo!!.roomUnit!! in tile.roomUnitsOnTile) return

        client.habbo!!.roomUnit!!.lookAt(tile)
    }

}