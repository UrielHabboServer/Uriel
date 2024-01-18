package org.urielserv.uriel.packets.incoming.rooms

import org.urielserv.uriel.RoomManager
import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.PacketHandler
import org.urielserv.uriel.packets.outgoing.rooms.RoomSettingsPacket
import java.nio.ByteBuffer

class RoomSettingsPacketHandler : PacketHandler {

    override suspend fun handle(client: UrielServerClient, packet: ByteBuffer) {
        val roomId = packet.getInt()
        val room = RoomManager.getRoom(roomId) ?: return

        RoomSettingsPacket(room).send(client)
    }

}