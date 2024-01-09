package org.urielserv.uriel.packets.incoming.rooms.user_unit

import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.PacketHandler
import java.nio.ByteBuffer

class RoomUnitLookPacketHandler : PacketHandler {

    override suspend fun handle(client: UrielServerClient, packet: ByteBuffer) {
        val habbo = client.habbo ?: return

        if (habbo.room == null) return

        val x = packet.getInt()
        val y = packet.getInt()

        val tile = habbo.room!!.tileMap!!.getTile(x, y) ?: return

        val roomUnit = habbo.roomUnit!!

        if (roomUnit in tile.roomUnitsOnTile) return

        if (roomUnit.isWalking && tile == roomUnit.previousTile) return

        roomUnit.lookAt(tile)
    }

}