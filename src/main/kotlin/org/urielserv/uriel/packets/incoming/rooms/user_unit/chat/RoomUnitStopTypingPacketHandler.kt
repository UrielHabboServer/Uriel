package org.urielserv.uriel.packets.incoming.rooms.user_unit.chat

import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.PacketHandler
import org.urielserv.uriel.packets.outgoing.rooms.user_unit.chat.RoomUnitTypingPacket
import java.io.ByteArrayInputStream

class RoomUnitStopTypingPacketHandler : PacketHandler {

    override suspend fun handle(client: UrielServerClient, packet: ByteArrayInputStream) {
        if (client.habbo == null) return

        if (client.habbo!!.roomUnit == null) return

        RoomUnitTypingPacket(client.habbo!!, false).broadcast(client.habbo!!.room!!)
    }

}