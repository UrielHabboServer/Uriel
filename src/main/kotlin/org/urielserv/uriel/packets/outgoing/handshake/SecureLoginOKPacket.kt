package org.urielserv.uriel.packets.outgoing.handshake

import org.urielserv.uriel.packets.Packet
import org.urielserv.uriel.packets.outgoing.OutgoingPacketIDs

class SecureLoginOKPacket : Packet {

    override val packetId = OutgoingPacketIDs.SecureLoginOK

    override suspend fun construct() = Unit

}