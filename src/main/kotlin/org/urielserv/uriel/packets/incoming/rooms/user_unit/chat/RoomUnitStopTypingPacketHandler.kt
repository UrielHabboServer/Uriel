package org.urielserv.uriel.packets.incoming.rooms.user_unit.chat

import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.PacketHandler
import org.urielserv.uriel.packets.outgoing.rooms.user_unit.chat.RoomUnitTypingPacket
import java.nio.ByteBuffer

class RoomUnitStopTypingPacketHandler : PacketHandler {

    override suspend fun handle(client: UrielServerClient, packet: ByteBuffer) {
        val habbo = client.habbo ?: return

        if (habbo.roomUnit == null) return

        RoomUnitTypingPacket(habbo, false).broadcast(habbo.room!!)
    }

}