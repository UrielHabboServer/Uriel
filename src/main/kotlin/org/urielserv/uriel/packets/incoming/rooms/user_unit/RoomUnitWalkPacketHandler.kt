package org.urielserv.uriel.packets.incoming.rooms.user_unit

import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.PacketHandler
import java.nio.ByteBuffer

class RoomUnitWalkPacketHandler : PacketHandler {

    override suspend fun handle(client: UrielServerClient, packet: ByteBuffer) {
        if (client.habbo == null) return

        if (client.habbo!!.roomUnit == null) return

        val x = packet.getInt()
        val y = packet.getInt()

        val tile = client.habbo!!.room!!.tileMap!!.getTile(x, y)

        if (tile == null) {
            client.habbo!!.room!!.leave(client.habbo!!)
            return
        }

        client.habbo!!.roomUnit!!.walkTo(tile)
    }

}