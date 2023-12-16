package org.urielserv.uriel.packets.incoming.rooms.user_unit

import org.urielserv.uriel.extensions.readInt
import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.PacketHandler
import java.io.ByteArrayInputStream

class RoomUnitWalkPacketHandler : PacketHandler {

    override suspend fun handle(client: UrielServerClient, packet: ByteArrayInputStream) {
        if (client.habbo == null) return

        if (client.habbo!!.roomUnit == null) return

        val x = packet.readInt()
        val y = packet.readInt()

        val tile = client.habbo!!.room!!.tileMap!!.getTile(x, y)

        if (tile == null) {
            client.habbo!!.room!!.leave(client.habbo!!)
            return
        }

        client.habbo!!.roomUnit!!.walkTo(tile)
    }

}