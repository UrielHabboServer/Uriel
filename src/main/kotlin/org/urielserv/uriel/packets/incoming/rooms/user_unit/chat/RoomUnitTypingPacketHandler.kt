package org.urielserv.uriel.packets.incoming.rooms.user_unit.chat

import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.PacketHandler
import org.urielserv.uriel.packets.outgoing.rooms.user_unit.chat.RoomUnitTypingPacket
import java.nio.ByteBuffer

class RoomUnitTypingPacketHandler : PacketHandler {

    override suspend fun handle(client: UrielServerClient, packet: ByteBuffer) {
        if (client.habbo == null) return

        if (client.habbo!!.roomUnit == null) return

        RoomUnitTypingPacket(client.habbo!!, true).broadcast(client.habbo!!.room!!)
    }

}