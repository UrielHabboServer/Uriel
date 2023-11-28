package org.urielserv.uriel.packets.outgoing.handshake

import org.urielserv.uriel.packets.outgoing.Packet
import org.urielserv.uriel.packets.outgoing.OutgoingPacketIDs

class PingPacket : Packet() {

    override val packetId = OutgoingPacketIDs.Ping

}