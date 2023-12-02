package org.urielserv.uriel.packets.outgoing.handshake

import org.urielserv.uriel.packets.outgoing.OutgoingPacketIDs
import org.urielserv.uriel.packets.outgoing.Packet

class ClientPingPacket : Packet() {

    override val packetId = OutgoingPacketIDs.ClientPing

    override suspend fun construct() = Unit

}