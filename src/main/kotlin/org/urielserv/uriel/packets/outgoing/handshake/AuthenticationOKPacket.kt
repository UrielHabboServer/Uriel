package org.urielserv.uriel.packets.outgoing.handshake

import org.urielserv.uriel.packets.outgoing.OutgoingPacketIDs
import org.urielserv.uriel.packets.outgoing.Packet

class AuthenticationOKPacket : Packet() {

    override val packetId = OutgoingPacketIDs.AuthenticationOK

    override suspend fun construct() = Unit

}