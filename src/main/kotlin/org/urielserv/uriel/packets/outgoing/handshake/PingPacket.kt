package org.urielserv.uriel.packets.outgoing.handshake

import org.urielserv.uriel.packets.Packet
import org.urielserv.uriel.packets.outgoing.OutgoingPacketIDs

class PingPacket : Packet {

    override val packetId = OutgoingPacketIDs.Ping

    override suspend fun construct() = Unit

}