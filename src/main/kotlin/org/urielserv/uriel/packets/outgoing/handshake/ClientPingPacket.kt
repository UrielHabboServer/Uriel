package org.urielserv.uriel.packets.outgoing.handshake

import org.urielserv.uriel.packets.outgoing.Outgoing
import org.urielserv.uriel.packets.outgoing.Packet

class ClientPingPacket : Packet() {

    override val packetId = Outgoing.ClientPing

    override suspend fun construct() = Unit

}