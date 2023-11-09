package org.urielserv.uriel.packets.incoming.handshake

import org.urielserv.uriel.extensions.readInt
import org.urielserv.uriel.extensions.readString
import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.PacketHandler
import java.io.ByteArrayInputStream

class ReleaseVersionPacketHandler : PacketHandler {

    override suspend fun handle(client: UrielServerClient, packet: ByteArrayInputStream) {
        packet.readString()
        packet.readString()
        packet.readInt()
        packet.readInt()
    }

}