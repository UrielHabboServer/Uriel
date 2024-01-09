package org.urielserv.uriel.packets.incoming.rooms

import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.PacketHandler
import org.urielserv.uriel.packets.outgoing.rooms.heightmap.RoomHeightmapPacket
import org.urielserv.uriel.packets.outgoing.rooms.heightmap.RoomModelPacket
import java.nio.ByteBuffer

class RoomModelPacketHandler : PacketHandler {

    override suspend fun handle(client: UrielServerClient, packet: ByteBuffer) {
        val habbo = client.habbo ?: return

        if (habbo.room == null) return

        RoomHeightmapPacket(habbo.room!!).send(client)
        RoomModelPacket(habbo.room!!).send(client)

        habbo.room!!.finishEnter(habbo)
    }

}