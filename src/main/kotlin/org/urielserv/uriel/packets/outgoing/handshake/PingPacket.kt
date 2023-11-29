package org.urielserv.uriel.packets.outgoing.handshake

import org.urielserv.uriel.packets.outgoing.OutgoingPacketIDs
import org.urielserv.uriel.packets.outgoing.Packet

class PingPacket : Packet() {

    override val packetId = OutgoingPacketIDs.Ping

    override suspend fun construct() = Unit

}