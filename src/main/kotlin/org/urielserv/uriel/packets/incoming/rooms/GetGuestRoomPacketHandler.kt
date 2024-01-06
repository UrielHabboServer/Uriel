package org.urielserv.uriel.packets.incoming.rooms

import org.urielserv.uriel.RoomManager
import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.PacketHandler
import org.urielserv.uriel.packets.outgoing.rooms.RoomInfoPacket
import java.nio.ByteBuffer

class GetGuestRoomPacketHandler : PacketHandler {

    override suspend fun handle(client: UrielServerClient, packet: ByteBuffer) {
        if (client.habbo == null) return

        val roomId = packet.getInt()
        val room = RoomManager.getRoomById(roomId) ?: return

        var enterRoom = packet.getInt() == 1
        val forwardRoom = packet.getInt() == 1

        if (!enterRoom && forwardRoom) {
            enterRoom = false
        }

        RoomInfoPacket(room, forwardRoom, enterRoom).send(client)
    }

}