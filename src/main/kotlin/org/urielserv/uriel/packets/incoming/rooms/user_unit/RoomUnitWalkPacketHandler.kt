package org.urielserv.uriel.packets.incoming.rooms.user_unit

import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.PacketHandler
import java.nio.ByteBuffer

class RoomUnitWalkPacketHandler : PacketHandler {

    override suspend fun handle(client: UrielServerClient, packet: ByteBuffer) {
        val habbo = client.habbo ?: return

        if (habbo.roomUnit == null) return

        val x = packet.getInt()
        val y = packet.getInt()

        val tile = habbo.room!!.tileMap!!.getTile(x, y)

        if (tile == null) {
            habbo.room!!.leave(habbo)
            return
        }

        habbo.roomUnit!!.walkTo(tile)
    }

}