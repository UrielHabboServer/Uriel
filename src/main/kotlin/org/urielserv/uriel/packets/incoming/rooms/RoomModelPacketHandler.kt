package org.urielserv.uriel.packets.incoming.rooms

import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.PacketHandler
import org.urielserv.uriel.packets.outgoing.rooms.heightmap.RoomHeightmapPacket
import org.urielserv.uriel.packets.outgoing.rooms.heightmap.RoomModelPacket
import java.nio.ByteBuffer

class RoomModelPacketHandler : PacketHandler {

    override suspend fun handle(client: UrielServerClient, packet: ByteBuffer) {
        if (client.habbo == null) return

        if (client.habbo!!.room == null) return

        RoomHeightmapPacket(client.habbo!!.room!!).send(client)
        RoomModelPacket(client.habbo!!.room!!).send(client)

        client.habbo!!.room!!.finishEnter(client.habbo!!)
    }

}